package com.techlab.proyecto_final_caruso_luciano.service;

import com.techlab.proyecto_final_caruso_luciano.dto.UserDTO;
import com.techlab.proyecto_final_caruso_luciano.model.Token;
import com.techlab.proyecto_final_caruso_luciano.model.User;
import com.techlab.proyecto_final_caruso_luciano.model.User.Role;
import com.techlab.proyecto_final_caruso_luciano.repository.TokenRepository;
import com.techlab.proyecto_final_caruso_luciano.repository.UserRepository;
import com.techlab.proyecto_final_caruso_luciano.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, JwtUtil jwtUtil)
    {
        this.userRepository  = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtUtil         = jwtUtil;
    }

    public User registerUser(UserDTO dto)
    {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (userRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        User.Role roleEnum;
        try {
            roleEnum = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El rol del usuario deber ser USER o ADMIN");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setDni(dto.getDni());
        user.setRole(roleEnum);

        return userRepository.save(user);
    }

    public String login(String email, String rawPassword)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña inválidos"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        Token token = new Token();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);

        return jwt;
    }


    /**
     * Trae a todos los usuarios desde la Base de datos
     *
     * @return
     */
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    /**
     * Trae los detalles de un usuario
     *
     * @param id
     * @return
     */
    public Optional<User> getUserById(Long id)
    {
        return userRepository.findById(id);
    }
}
