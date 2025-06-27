package com.techlab.proyecto_final_caruso_luciano.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;

@Entity
@Getter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Positive(message = "EL precio debe ser mayor a 0")
    private double price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}

