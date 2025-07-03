package com.techlab.proyecto_final_caruso_luciano.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
}
