package com.techlab.proyecto_final_caruso_luciano.dto.response;

import com.techlab.proyecto_final_caruso_luciano.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO
{
    private Long id;
    private List<ProductSummaryDTO> products;
    private double total;
    private Order.OrderState state;
    private LocalDateTime date;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.total = order.getTotal();
        this.state = order.getState();
        this.date = order.getDate();
        this.products = order.getProducts().stream()
                .map(ProductSummaryDTO::new)
                .toList();
    }
}
