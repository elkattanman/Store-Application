package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.domain.StoreHasProduct;
import com.elkattanman.javafxapp.domain.StoreHasProductID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreProductRepository extends JpaRepository<StoreHasProduct, StoreHasProductID> {

    List<StoreHasProduct> findAllByProduct(Product product);
    List<StoreHasProduct> findAllByProduct_Id(Integer id);
    List<StoreHasProduct> findAllByStore(Product product);
    List<StoreHasProduct> findAllByStore_Id(Integer id);
}
