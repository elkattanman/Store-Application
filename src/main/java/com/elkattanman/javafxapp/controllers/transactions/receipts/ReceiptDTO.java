package com.elkattanman.javafxapp.controllers.transactions.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {
    private Integer id;
    private String supplierName;
    private String storeName;
    private String userName;
    private LocalDate date;
    private Double Total;
}
