package com.example.paygate.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateTransactionRequest(
    @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
    @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
    @NotBlank @Size(max = 100) String idempotencyKey
) {}
