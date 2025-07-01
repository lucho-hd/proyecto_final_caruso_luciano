package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.dto.ProductDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseWithData<List<Product>>> getAllProducts()
    {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponseWithData<>("Listado de productos", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWithData<?>> getProductById(@PathVariable Long id)
    {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(new ApiResponseWithData<>("Producto encontrado: ", product.get()));
        } else {
            return  ResponseEntity.status(404).body(new ApiResponseWithData<>("Producto no encontrado", null));
        }
    }
}
