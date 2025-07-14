package com.techlab.proyecto_final_caruso_luciano.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductId implements Serializable {
    private Long orderId;
    private Long productId;
}
