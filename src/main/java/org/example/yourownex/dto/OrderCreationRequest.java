package org.example.yourownex.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreationRequest {
    private String type;
    private String currency;
    private BigDecimal price;
    private BigDecimal amount;
}
