package com.techlab.proyecto_final_caruso_luciano.repository;

import com.techlab.proyecto_final_caruso_luciano.model.Token;
import com.techlab.proyecto_final_caruso_luciano.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>
{
    List<Token> findAllByUser(User user);
    Optional<Token> findByToken(String token);
}
