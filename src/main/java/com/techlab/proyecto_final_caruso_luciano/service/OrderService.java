package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.OrderDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.OrderResponseDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.ProductOrderDTO;
import com.techlab.proyecto_final_caruso_luciano.dto.response.ProductSummaryDTO;
import com.techlab.proyecto_final_caruso_luciano.model.*;
import com.techlab.proyecto_final_caruso_luciano.repository.OrderProductRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.OrderRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.ProductRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        UserRepository userRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderDTO dto) {
        Map<Long, Integer> quantityMap = dto.getProducts().stream()
                .collect(Collectors.toMap(ProductOrderDTO::getProductId, ProductOrderDTO::getQuantity));

        List<Product> products = productRepository.findAllById(quantityMap.keySet());
        if (products.size() != quantityMap.size()) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }

        for (Product product : products) {
            int requestedQty = quantityMap.get(product.getId());
            if (requestedQty > product.getStock()) {
                throw new IllegalArgumentException("Producto \"" + product.getName() + "\" no tiene suficiente stock");
            }
            product.setStock(product.getStock() - requestedQty);
        }
        productRepository.saveAll(products);

        // Obtener usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Order order = new Order();
        order.setUser(user);
        order.setDate(LocalDateTime.now());
        order.setState(dto.getState());
        order.setTotal(0d); // temporal
        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        double total = 0;

        for (Product product : products) {
            int qty = quantityMap.get(product.getId());
            OrderProduct op = new OrderProduct();
            op.setOrder(savedOrder);
            op.setProduct(product);
            op.setQuantity(qty);
            op.setId(new OrderProductId(savedOrder.getId(), product.getId()));

            orderProducts.add(op);

            total += product.getPrice() * qty;
        }

        orderProductRepository.saveAll(orderProducts);

        savedOrder.setTotal(total);
        savedOrder.setOrderProducts(orderProducts);
        orderRepository.save(savedOrder);

        List<ProductSummaryDTO> productSummaries = orderProducts.stream()
                .map(op -> new ProductSummaryDTO(
                        op.getProduct().getId(),
                        op.getProduct().getName(),
                        op.getProduct().getPrice(),
                        op.getQuantity()
                ))
                .toList();

        return new OrderResponseDTO(
                savedOrder.getId(),
                productSummaries,
                savedOrder.getTotal(),
                savedOrder.getState(),
                savedOrder.getDate()
        );
    }
}

