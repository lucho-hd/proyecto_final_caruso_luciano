package com.techlab.proyecto_final_caruso_luciano.repository;

import com.techlab.proyecto_final_caruso_luciano.model.Order;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
