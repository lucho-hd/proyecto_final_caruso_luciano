package com.techlab.proyecto_final_caruso_luciano.controller.admin;

import com.techlab.proyecto_final_caruso_luciano.dto.CategoryDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Category;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoriesController {

    private final CategoryService categoryService;

    public AdminCategoriesController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    /**
     * Devulve el listado de las categorías.
     *
     * @return - Lista de categorías.
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponseWithData<List<Category>>> getAllCategories()
    {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponseWithData<>("Listado de categorías", categories));
    }

    /**
     * Crea una nueva categoría en la base de datos
     *
     * @param dto - Datos de la nueva categoría.
     * @return - Nueva categoría.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponseWithData<Category>> create(@Valid @RequestBody CategoryDTO dto)
    {
        Category saved = categoryService.createCategory(dto);
        return ResponseEntity.ok(new ApiResponseWithData<>("¡Categoría creada exitosamente!", saved));
    }

    /**
     * Edita una categoría en la base de datos.
     *
     * @param id - el id de la categoría
     * @param dto - Datos recibidos de la categoría
     * @return - Categoría editada.
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponseWithData<Category>> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto)
    {
        Optional<Category> updated = categoryService.updateCategory(id, dto);
        return updated.map(value ->
                ResponseEntity.ok(new ApiResponseWithData<>("¡Categoría actualizada exitosamente!", value))
        ).orElseGet(() ->
                ResponseEntity.status(404).body(new ApiResponseWithData<>("Categoría no encontrada", null))
        );
    }

    /**
     * Elimina una categoría de la base de datos.
     *
     * @param id - el id de la categoría a eliminar
     * @return - Retorna mensaje de éxito al eliminar o 404 si no encuentra la categoría.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponseWithData<String>> delete(@PathVariable Long id)
    {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponseWithData<>("¡Categoría eliminada exitosamente!", null));
        }
        return ResponseEntity.status(404).body(new ApiResponseWithData<>("Categoría no encontrada", null));
    }
}
