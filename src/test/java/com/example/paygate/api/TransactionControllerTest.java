package com.example.paygate.api;

import com.example.paygate.model.Transaction;
import com.example.paygate.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(com.example.paygate.config.SecurityConfig.class)
class TransactionControllerTest {
    @Autowired MockMvc mvc;
    @MockBean TransactionRepository repository;

    @Test void createsTransaction() throws Exception {
        when(repository.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        mvc.perform(post("/api/transactions").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("demo","change-me"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"amount\":12.50,\"currency\":\"USD\",\"idempotencyKey\":\"key-1\"}"))
            .andExpect(status().isCreated());
    }

    @Test void returnsExistingTransactionForRepeatedKey() throws Exception {
        Transaction existing = new Transaction(new BigDecimal("12.50"), "USD", "key-1");
        when(repository.findByIdempotencyKey("key-1")).thenReturn(Optional.of(existing));
        mvc.perform(post("/api/transactions").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("demo","change-me"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"amount\":12.50,\"currency\":\"USD\",\"idempotencyKey\":\"key-1\"}"))
            .andExpect(status().isOk());
    }
}
