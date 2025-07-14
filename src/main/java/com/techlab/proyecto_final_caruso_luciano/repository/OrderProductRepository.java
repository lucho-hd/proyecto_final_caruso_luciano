package com.techlab.proyecto_final_caruso_luciano.repository;

import com.techlab.proyecto_final_caruso_luciano.model.OrderProduct;
import com.techlab.proyecto_final_caruso_luciano.model.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
}
