package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.CategoryDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Category;
import com.techlab.proyecto_final_caruso_luciano.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Crea una nueva categoría en la base de datos.
     *
     * @param dto - Los datos de la nueva categoría.
     * @return - Nueva categoría.
     */
    public Category createCategory(CategoryDTO dto)
    {
        if (categoryRepository.existsByName(dto.getName())) {
            throw  new IllegalArgumentException("La categoría ya existe");
        }

        Category category = new Category();
        category.setName(dto.getName());
        return  categoryRepository.save(category);
    }

    /**
     * Devuelve el listado de las categorías.
     *
     * @return - Lista de categorías.
     */
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    /**
     * Actualiza una categoría en la base de datos.
     *
     * @param id - El id de la categoría a actualizar.
     * @param dto - Los datos de la categoría.
     * @return - Categoría actualizada.
     */
    public Optional<Category> updateCategory(Long id, CategoryDTO dto)
    {
        return categoryRepository.findById(id).map(category -> {
            category.setName(dto.getName());

            return categoryRepository.save(category);
        });
    }

    /**
     * Elimina una categoría por su id de la Base de datos.
     *
     * @param id - El id de la categoría.
     * @return - La categoría eliminada
     */
    public boolean deleteCategory(Long id)
    {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
