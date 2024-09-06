package org.example.yourownex.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private String currency;
    private BigDecimal amount;
    private String address;
}
