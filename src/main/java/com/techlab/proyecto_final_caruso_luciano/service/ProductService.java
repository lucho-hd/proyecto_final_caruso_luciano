package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.ProductDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Product;
import com.techlab.proyecto_final_caruso_luciano.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Obtiene todos los productos de la BD
     *
     * @return
     */
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
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

    /**
     * Crea un nuevo producto en la Base de datos
     *
     * @param dto
     * @return
     */
    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto en la Base de datos mediante su id
     *
     * @param id
     * @param dto
     * @return
     */
    public Optional<Product> updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id).map(product -> {
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setStock(dto.getStock());
            return productRepository.save(product);
        });
    }

    /**
     * Elimina un producto de la Base de datos.
     *
     * @param id
     * @return
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
