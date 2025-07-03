package com.techlab.proyecto_final_caruso_luciano.dto;

import com.techlab.proyecto_final_caruso_luciano.model.Order;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    @NotEmpty(message = "Debes seleccionar al menos un producto")
    private List<Long> productsIds;

    @NotNull(message = "Debes especificar el estado del pedido")
    private Order.OrderState state;
}
