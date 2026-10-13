package com.example.paygate.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_tx_created_at", columnList = "createdAt"),
    @Index(name = "idx_tx_status", columnList = "status")
})
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false, length = 32)
    private String status;
    @Column(nullable = false, unique = true, length = 100)
    private String idempotencyKey;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Transaction() {}
    public Transaction(BigDecimal amount, String currency, String idempotencyKey) {
        this.amount = amount; this.currency = currency; this.idempotencyKey = idempotencyKey; this.status = "ACCEPTED";
    }
    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Instant getCreatedAt() { return createdAt; }
    public void setStatus(String status) { this.status = status; }
}
