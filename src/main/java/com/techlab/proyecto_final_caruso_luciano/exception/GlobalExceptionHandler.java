package com.techlab.proyecto_final_caruso_luciano.exception;

import com.techlab.proyecto_final_caruso_luciano.response.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String path = request.getRequestURI();

        List<String> requiredFields;

        if (path.contains("/api/auth/register")) {
            requiredFields = List.of("name", "surname", "email", "password", "dni", "address");
        } else if (path.contains("/api/products")) {
            requiredFields = List.of("name", "description", "price", "stock");
        } else {
            requiredFields = List.of("Verificá la documentación para conocer los campos requeridos.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "El cuerpo de la solicitud está vacío o tiene un formato incorrecto.");
        response.put("required_fields", requiredFields);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Faltan campos requeridos o hay datos inválidos");
        response.put("errors", errorList);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
