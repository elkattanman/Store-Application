package com.elkattanman.javafxapp.domain;


import lombok.*;

import javax.persistence.*;

//@Data
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Receipt_id", referencedColumnName = "id")
    ReceiptHeader receiptHeader;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;
    private int quantity;

    public double anTotal(){
        return quantity*product.getPrice();
    }
}
