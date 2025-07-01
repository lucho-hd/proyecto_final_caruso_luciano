package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.dto.LoginDto;
import com.techlab.proyecto_final_caruso_luciano.dto.UserDTO;
import com.techlab.proyecto_final_caruso_luciano.model.User;
import com.techlab.proyecto_final_caruso_luciano.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registro de un usuario
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.registerUser(userDTO);
            return ResponseEntity.ok().body(Map.of(
                    "message", "¡Usuario registrado correctamente!",
                    "user", Map.of("id", user.getId(), "email", user.getEmail())
            ));
        } catch (IllegalArgumentException ex) {
            // Este error lo maneja directamente el controller
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    /**
     * Realiza el login del usuario
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto request)
    {
        try {
            String token = userService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(Map.of("Erorr", ex.getMessage()));
        }
    }
}
