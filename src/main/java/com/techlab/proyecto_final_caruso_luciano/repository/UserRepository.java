package com.techlab.proyecto_final_caruso_luciano.repository;

import com.techlab.proyecto_final_caruso_luciano.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    Optional<User> findByEmail(String email);
}
