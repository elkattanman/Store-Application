package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.ReceiptHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<ReceiptHeader, Integer> {
}
