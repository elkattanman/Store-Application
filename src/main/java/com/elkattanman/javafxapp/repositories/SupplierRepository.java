package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
