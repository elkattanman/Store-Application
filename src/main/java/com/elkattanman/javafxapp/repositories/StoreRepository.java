package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
