package com.techlab.proyecto_final_caruso_luciano.dto;

import jakarta.validation.constraints.*;

public class ProductDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "La descripción no puede quedar vacía")
    @Size(max = 255, message = "La descripción tiene un máximo de 255 caracteres")
    private String description;

    @Positive(message = "El precio debe ser mayor a 0")
    private double price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }


    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
