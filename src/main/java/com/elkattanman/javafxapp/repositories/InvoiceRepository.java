package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.InvoiceHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceHeader, Integer> {
    
}
