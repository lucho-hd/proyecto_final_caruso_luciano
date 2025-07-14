package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.ProductDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Category;
import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.repository.CategoryRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository  = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Obtiene todos los productos de la BD
     *
     * @return - Lista de productos.
     */
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String categoryName)
    {
        return productRepository.findByCategoriesNameIgnoreCase(categoryName);
    }

    /**
     * Devuelve el detalle de un producto mediante su id
     *
     * @param id
     * @return
     */
    public Optional<Product> getProductById(Long id)
    {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Crea un nuevo producto en la Base de datos
     *
     * @param dto - Datos del producto a crear desde el Front.
     * @return - Producto creado.
     */
    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        Set<Long> ids = dto.getCategoryIds();
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

        if (categories.size() != ids.size()) {
            throw new IllegalArgumentException("Una o más categorías no existen");
        }

        product.setCategories(categories);

        return productRepository.save(product);
    }

    /**
     * Actualiza un producto en la Base de datos mediante su id
     *
     * @param id - El id del producto a editar.
     * @param dto - Datos del producto del Front.
     * @return - El producto actualizado.
     */
    public Optional<Product> updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id).map(product -> {
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setStock(dto.getStock());

            Set<Long> ids = dto.getCategoryIds();
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

            if (categories.size() != ids.size()) {
                throw new IllegalArgumentException("Una o más categorías no existen");
            }
            product.setCategories(categories);

            return productRepository.save(product);
        });
    }

    /**
     * Elimina un producto de la Base de datos.
     *
     * @param id - El id del producto a eliminar.
     * @return - Producto eliminado.
     */
    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            String imageUrl = product.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    Path imagePath = Paths.get("uploads").resolve(fileName);

                    Files.deleteIfExists(imagePath);
                    System.out.println("🗑️ Imagen eliminada del disco: " + imagePath);
                } catch (IOException e) {
                    System.err.println("⚠️ Error al eliminar imagen: " + e.getMessage());
                }
            }
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
