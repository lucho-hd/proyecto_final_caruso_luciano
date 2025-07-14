package com.techlab.proyecto_final_caruso_luciano.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class ProductDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 255, message = "El nombre del producto debe tener entre 2 y 255 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede quedar vacía")
    private String description;

    @Positive(message = "El precio debe ser mayor a 0")
    private double price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @NotEmpty(message = "Debes de seleccionar al menos una categoría para el producto")
    private Set<Long> categoryIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }


    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
