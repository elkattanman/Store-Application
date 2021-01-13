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

}
