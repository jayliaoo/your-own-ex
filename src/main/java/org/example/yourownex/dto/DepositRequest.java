package org.example.yourownex.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private BigDecimal amount;
}
