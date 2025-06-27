package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.repository.ProductRepository;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductoController {
    private final ProductRepository productRepository;

    public ProductoController(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @GetMapping("/list")
    /**
     * Obtiene todos los productos desde la BD.
     */
    public List<Product> getAll()
    {
        return productRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id)
    {
        Optional<Product> product = productRepository.findById(id);
        return  product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    /**
     * Crea un nuevo producto en la BD
     */
    public ResponseEntity<?> create(@Valid @RequestBody Product product)
    {
        try {
            Product saved =  productRepository.save(product);
            ApiResponseWithData<Product> response = new ApiResponseWithData<>("¡Producto creado exitosamente!", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return  ResponseEntity.status(500).body(new ApiResponseWithData<String>("Error al crear el producto: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponseWithData<Object>> update(@PathVariable Long id, @RequestBody Product updatedData) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedData.getName());
                    product.setPrice(updatedData.getPrice());
                    product.setStock(updatedData.getStock());
                    Product saved = productRepository.save(product);
                    ApiResponseWithData<Object> response = new ApiResponseWithData<>("Producto actualizado con éxito", saved);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseWithData<Object> response = new ApiResponseWithData<>("Producto no encontrado", null);
                    return ResponseEntity.status(404).body(response);
                });
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponseWithData<?>> delete(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            ApiResponseWithData<String> response = new ApiResponseWithData<>("Producto eliminado con éxito", null);
            return ResponseEntity.ok(response);
        }
        ApiResponseWithData<String> response = new ApiResponseWithData<>("Producto no encontrado", null);
        return ResponseEntity.status(404).body(response);
    }
}
