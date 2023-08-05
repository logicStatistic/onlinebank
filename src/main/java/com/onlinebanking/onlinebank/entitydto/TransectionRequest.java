package com.onlinebanking.onlinebank.entitydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransectionRequest {
    private String accountNumber;
    private BigDecimal amount;
}
