package com.techlab.proyecto_final_caruso_luciano.controller.admin;

import com.techlab.proyecto_final_caruso_luciano.dto.response.OrderResponseDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Order;
import com.techlab.proyecto_final_caruso_luciano.repository.OrderRepository;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    /**
     * Devuelve los datos de un pedido.
     *
     * @param id - El id del pedido.
     * @return - Datos del pedido.
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponseWithData<OrderResponseDTO>> getOrderDetails(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseWithData<>("Pedido no encontrado", null));
        }

        OrderResponseDTO dto = new OrderResponseDTO(optionalOrder.get());
        return ResponseEntity.ok(new ApiResponseWithData<>("Detalles del pedido", dto));
    }
}
