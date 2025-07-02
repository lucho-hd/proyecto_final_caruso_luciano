package com.techlab.proyecto_final_caruso_luciano.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(message = "El nombre de la categoría debe tener al menos dos caracteres.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
