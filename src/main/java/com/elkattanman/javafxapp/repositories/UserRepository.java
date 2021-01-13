package com.elkattanman.javafxapp.repositories;

import com.elkattanman.javafxapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
