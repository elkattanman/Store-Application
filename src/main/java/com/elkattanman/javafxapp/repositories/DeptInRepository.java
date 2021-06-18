package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.DeptIn;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeptInRepository extends JpaRepository<DeptIn, Integer> {

     DeptIn findByDescriptionContaining(String description);
}
