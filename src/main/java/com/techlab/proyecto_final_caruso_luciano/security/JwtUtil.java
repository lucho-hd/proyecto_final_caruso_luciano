package com.techlab.proyecto_final_caruso_luciano.security;

import com.techlab.proyecto_final_caruso_luciano.model.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirations;

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *
     * @param email - El email del usuario.
     * @param role - El rol del usuario.
     * @return - Token
     */
    public String generateToken(String email, String role)
    {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirations))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * @param token - Token
     * @return - Retorna true si el token o false en caso contrario.
     */
    public boolean isTokenValid(String token)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrae el email del usuario del token.
     *
     * @param token - El token proporcionado.
     * @return - El email del usuario.
     */
    public String extractEmail(String token)
    {
        if (token == null || token.split("\\.").length != 3) {
            throw new MalformedJwtException("Formato de token inválido");
        }

        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     *
     * @param token - El token.
     * @return - El rol del usuario.
     */
    public String extractRole(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
