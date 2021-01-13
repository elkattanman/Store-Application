package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
