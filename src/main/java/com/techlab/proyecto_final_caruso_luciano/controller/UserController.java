package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.model.User;
import com.techlab.proyecto_final_caruso_luciano.response.ApiResponseWithData;
import com.techlab.proyecto_final_caruso_luciano.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    // Ver que onda esto...
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<ApiResponseWithData<List<User>>> getAllUsers()
    {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseWithData<>("Listado de usuarios", users));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWithData<?>> getUserById(@PathVariable Long id)
    {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponseWithData<>("Usuario encontrado: ", user.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponseWithData<>("Usuario no encontrado", null));
        }
    }
}
