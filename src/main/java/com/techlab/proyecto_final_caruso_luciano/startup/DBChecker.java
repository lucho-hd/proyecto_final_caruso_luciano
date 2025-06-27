package com.techlab.proyecto_final_caruso_luciano.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBChecker implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String ...args) {
        System.out.println("📣 Entrando a DBChecker.run()");
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("✅ Conexión exitosa. Resultado: " + count);
        } catch (Exception e) {
            System.out.println("❌ Error de conexión con la BD: " + e.getMessage());
        }
    }
}
