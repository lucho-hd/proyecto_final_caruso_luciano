package com.techlab.proyecto_final_caruso_luciano.dto.response;

import com.techlab.proyecto_final_caruso_luciano.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
