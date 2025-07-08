package com.techlab.proyecto_final_caruso_luciano.controller.admin;

import com.techlab.proyecto_final_caruso_luciano.dto.ProductDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Crea un nuevo producto.
     *
     * @param dto - Datos del producto a crear recibidos desde el Front.
     * @return - Producto creado.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponseWithData<Product>> create(@Valid @RequestBody ProductDTO dto) {
        Product saved = productService.createProduct(dto);
        return ResponseEntity.ok(new ApiResponseWithData<>("Producto creado exitosamente", saved));
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        Product product = optionalProduct.get();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Ruta absoluta para uploads, se puede cambiar si querés otra ubicación
        Path uploadDir = Paths.get("uploads").toAbsolutePath();

        // Crear la carpeta uploads si no existe
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(fileName);

        // Copiar el archivo al directorio uploads
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Guardar la URL absoluta hardcodeada (ajustá el dominio si cambia)
        product.setImageUrl("http://localhost:8081/uploads/" + fileName);

        productService.save(product);

        return ResponseEntity.ok("¡Imagen subida correctamente!");
    }

    /**
     * Edita los datos de un producto.
     *
     * @param id  - Id del producto a editar.
     * @param dto - Datos recibidos desde el Front.
     * @return - Producto editado
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponseWithData<Product>> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        Optional<Product> updated = productService.updateProduct(id, dto);
        return updated.map(value ->
                ResponseEntity.ok(new ApiResponseWithData<>("Producto actualizado", value))
        ).orElseGet(() ->
                ResponseEntity.status(404).body(new ApiResponseWithData<>("Producto no encontrado", null))
        );
    }

    /**
     * Elimina los datos de un producto.
     *
     * @param id - El id del producto a eliminar.
     * @return - Retorna mensaje de éxito al eliminar o 404 en caso de no encotrar el producto a eliminar.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponseWithData<String>> delete(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponseWithData<>("Producto eliminado correctamente", null));
        }
        return ResponseEntity.status(404).body(new ApiResponseWithData<>("Producto no encontrado", null));
    }
}
