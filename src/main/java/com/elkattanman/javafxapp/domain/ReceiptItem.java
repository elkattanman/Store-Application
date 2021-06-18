package com.elkattanman.javafxapp.domain;


import lombok.*;

import javax.persistence.*;

//@Data
@Setter
@Getter
@Entity
//@EntityListeners(ReceiptItemAudit.class)
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

    @Override
    public String toString() {
        return "ReceiptItem{" +
                "id=" + id +
                ", receiptHeader=" + receiptHeader.getId() +
                ", product=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
