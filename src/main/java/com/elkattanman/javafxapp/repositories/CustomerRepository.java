package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
