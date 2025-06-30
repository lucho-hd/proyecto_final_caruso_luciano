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

    @PostMapping("/create")
    public ResponseEntity<ApiResponseWithData<Product>> create(@Valid @RequestBody ProductDTO dto)
    {
        Product saved = productService.createProduct(dto);
        return ResponseEntity.ok(new ApiResponseWithData<>("Producto creado exitosamente", saved));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponseWithData<?>> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto)
    {
        Optional<Product> updated = productService.updateProduct(id, dto);
        if (updated.isPresent()) {
            return ResponseEntity.ok(new ApiResponseWithData<>("Producto actualizado", updated.get()));
        }
        return ResponseEntity.status(404).body(new ApiResponseWithData<>("Producto no encontrado", null));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponseWithData<String>> delete(@PathVariable Long id)
    {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponseWithData<>("Producto eliminado correctamente", null));
        }
        return ResponseEntity.status(404).body(new ApiResponseWithData<>("Producto no encontrado", null));
    }
}
