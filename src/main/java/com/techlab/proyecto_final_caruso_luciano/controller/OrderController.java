package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.dto.OrderDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.OrderResponseDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Order;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.OrderService;
import com.techlab.proyecto_final_caruso_luciano.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService)
    {
        this.orderService = orderService;
        this.userService  = userService;
    }

    /**
     * Crea un nuevo pedido para el usuario autenticado.
     *
     * @param dto - Los datos del pedido.
     * @return - Pedido creado.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponseWithData<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderDTO dto)
    {
        OrderResponseDTO created = orderService.createOrder(dto);
        return ResponseEntity.ok(new ApiResponseWithData<>("¡Pedido creado exitosamente!", created));
    }
}
