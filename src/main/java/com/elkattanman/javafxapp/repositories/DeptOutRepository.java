package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.DeptIn;
import com.elkattanman.javafxapp.domain.DeptOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptOutRepository extends JpaRepository<DeptOut, Integer> {
    DeptOut findByDescriptionContaining(String description);
}
