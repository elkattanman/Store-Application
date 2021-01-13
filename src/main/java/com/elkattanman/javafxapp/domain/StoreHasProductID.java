package com.elkattanman.javafxapp.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
//@Builder
@Embeddable
public class StoreHasProductID implements Serializable {

    @Builder
    public StoreHasProductID(Integer storeId, Integer productId) {
        this.storeId = storeId;
        this.productId = productId;
    }

    private Integer storeId;
    private Integer productId;
}
