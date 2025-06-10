package com.module.pix.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        String message = "Formato inválido: verifique se os campos numéricos estão corretos";

        if (ex.getCause() instanceof InvalidFormatException formatEx) {
            String fieldName = formatEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("campo"); // Caso não consiga, usa "campo" genérico

            message = String.format("Campo '%s' está em formato inválido", fieldName);
        }

        return ResponseEntity.unprocessableEntity().body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.unprocessableEntity().body(errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        Object invalidValue = ex.getValue();
        Class<?> expectedType = ex.getRequiredType();

        String expectedTypeName = (expectedType != null) ? expectedType.getSimpleName() : "tipo desconhecido";

        String message = String.format("Parâmetro '%s' com valor inválido: '%s'. Esperado tipo: %s.",
                paramName, invalidValue, expectedTypeName);

        return ResponseEntity.badRequest().body(Map.of("error", message));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<Map<String, String>> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(Map.of("error", ex.getMessage()));
    }


}
