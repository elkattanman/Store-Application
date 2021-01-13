package com.elkattanman.javafxapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private String branch;
    private String phone;

//    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "store")
//    private Set<StoreHasProduct> storeHasProducts;
}
