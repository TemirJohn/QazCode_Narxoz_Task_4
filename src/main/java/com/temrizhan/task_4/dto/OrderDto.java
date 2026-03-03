package com.temrizhan.task_4.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderDto(
        @NotNull(message = "Customer ID is required") Long customerId,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than 0") BigDecimal amount
) {}