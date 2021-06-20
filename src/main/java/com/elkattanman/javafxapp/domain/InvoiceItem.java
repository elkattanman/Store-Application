package com.elkattanman.javafxapp.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
//@EntityListeners(ReceiptItemAudit.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    InvoiceHeader invoiceHeader;

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
                ", invoiceHeader=" + invoiceHeader.getId() +
                ", product=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
