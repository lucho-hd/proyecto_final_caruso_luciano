package com.techlab.proyecto_final_caruso_luciano.controller;

import com.techlab.proyecto_final_caruso_luciano.dto.LoginDto;
import com.techlab.proyecto_final_caruso_luciano.dto.UserDTO;
import com.techlab.proyecto_final_caruso_luciano.model.User;
import com.techlab.proyecto_final_caruso_luciano.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crea un nuevo usuario en la BD.
     *
     * @param userDTO - Los datos del usuario provenientes del front.
     * @return - Registro del usuario.
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
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()
            ));
        }
    }

    /**
     * Realiza el login del usuario
     *
     * @param request - Los datos del usuario para iniciar su sesión.
     * @return - Login del usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto request) {
        try {
            // Autenticar y generar token
            String token = userService.login(request.getEmail(), request.getPassword());

            // Obtener el usuario autenticado (ya lo tenés validado)
            User user = userService.findByEmail(request.getEmail());

            // Crear una respuesta con token y datos del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "_id", user.getId(),
                    "name", user.getName(),
                    "surname", user.getSurname(),
                    "role", user.getRole()
            ));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader)
    {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token inválido"));
        }

        String token = authHeader.substring(7);

        try {
            userService.logout(token);
            return ResponseEntity.ok(Map.of("message", "¡Sesión cerrada exitosamente!"));
        } catch (IllegalArgumentException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
