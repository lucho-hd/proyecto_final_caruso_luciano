package com.techlab.proyecto_final_caruso_luciano.repository;

import com.techlab.proyecto_final_caruso_luciano.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
