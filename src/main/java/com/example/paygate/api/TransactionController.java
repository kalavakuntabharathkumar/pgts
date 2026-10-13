package com.example.paygate.api;

import com.example.paygate.model.Transaction;
import com.example.paygate.repo.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository repository;
    public TransactionController(TransactionRepository repository) { this.repository = repository; }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTransactionRequest request) {
        Optional<Transaction> existing = repository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) return ResponseEntity.ok(existing.get());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(
                new Transaction(request.amount(), request.currency(), request.idempotencyKey())));
        } catch (DataIntegrityViolationException race) {
            return repository.findByIdempotencyKey(request.idempotencyKey())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseThrow(() -> race);
        }
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @GetMapping
    public List<Transaction> list() { return repository.findAll(); }

    @PatchMapping("/{id}/status")
    public Transaction updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!Set.of("ACCEPTED", "SETTLED", "FAILED", "REFUNDED").contains(status))
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported status");
        Transaction tx = repository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND));
        tx.setStatus(status);
        return repository.save(tx);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
