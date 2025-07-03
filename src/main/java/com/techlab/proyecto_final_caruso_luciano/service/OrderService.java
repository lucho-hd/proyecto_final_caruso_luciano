package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.OrderDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.OrderResponseDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.ProductSummaryDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Order;
import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.model.User;
import com.techlab.proyecto_final_caruso_luciano.repository.OrderRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.ProductRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository)
    {
        this.orderRepository   = orderRepository;
        this.productRepository = productRepository;
        this.userRepository    = userRepository;
    }

    /**
     * Crea una nueva order
     *
     * @param dto - Datos de la orden.
     * @return - Retorna la orden creada.
     */
    public OrderResponseDTO createOrder(OrderDTO dto)
    {
        List<Product> products = productRepository.findAllById(dto.getProductsIds());

        if (products.size() != dto.getProductsIds().size()) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        double total = products.stream().mapToDouble(Product::getPrice).sum();

        Order order = new Order();
        order.setProducts(products);
        order.setTotal(total);
        order.setState(dto.getState());
        order.setDate(LocalDateTime.now());
        order.setUser(user);

        Order savedOrder = orderRepository.save(order);

        List<ProductSummaryDTO> productSummaries = products.stream()
                .map(p -> new ProductSummaryDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock()
                )).toList();

        return new OrderResponseDTO(
                savedOrder.getId(),
                productSummaries,
                savedOrder.getTotal(),
                savedOrder.getState(),
                savedOrder.getDate()
        );
    }
}
