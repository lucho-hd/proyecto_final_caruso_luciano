package com.techlab.proyecto_final_caruso_luciano.controller.admin;

import com.techlab.proyecto_final_caruso_luciano.dto.response.OrderResponseDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Order;
import com.techlab.proyecto_final_caruso_luciano.repository.OrderRepository;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrdersController {
    private final OrderRepository orderRepository;

    @Autowired
    public AdminOrdersController(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseWithData<List<OrderResponseDTO>>> getAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> dtoList = orders.stream()
                .map(OrderResponseDTO::new)
                .toList();
        return ResponseEntity.ok(new ApiResponseWithData<>("Listado de ordenes:", dtoList));
    }
}
