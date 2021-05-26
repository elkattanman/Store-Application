package com.elkattanman.javafxapp.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Builder
@Entity
public class StoreHasProduct {

    @EmbeddedId
    private StoreHasProductID storeHasProductID;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "storeId")
    @MapsId("storeId")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "productId")
    @MapsId("productId")
    private Product product;

    public void addQuantity(int val){
        quantity+=val;
    }

    public void subQuantity(int val) throws Exception {
        if(val > quantity) throw new Exception("Value greater than current quantity in store");
        else quantity-=val;
    }
}
