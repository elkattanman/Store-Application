package com.elkattanman.javafxapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer productId;
    private String productName;
    private String productType;
    private Double productPrice;
    private Integer productQnt;
    private Double total;
}
