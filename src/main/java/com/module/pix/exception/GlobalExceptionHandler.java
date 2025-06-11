package com.module.pix.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        String message = "Formato inválido: verifique se os campos numéricos estão corretos";

        if (ex.getCause() instanceof InvalidFormatException formatEx) {
            String fieldName = formatEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("campo");

            message = String.format("Campo '%s' está em formato inválido", fieldName);
        }

        log.warn("Erro de leitura da mensagem JSON: {}", message, ex);
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("Erro de validação: {}", errors);
        return ResponseEntity.unprocessableEntity().body(errors);
    }

    @ExceptionHandler({
            ValidationException.class,
            UnprocessableEntityException.class
    })
    public ResponseEntity<Map<String, String>> handle422Exceptions(RuntimeException ex) {
        log.warn("Exceção 422 tratada: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(ResourceNotFoundException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        Object invalidValue = ex.getValue();
        Class<?> expectedType = ex.getRequiredType();
        String expectedTypeName = (expectedType != null) ? expectedType.getSimpleName() : "tipo desconhecido";

        String message = String.format("Parâmetro '%s' com valor inválido: '%s'. Esperado tipo: %s.",
                paramName, invalidValue, expectedTypeName);

        log.warn("Erro de tipo em parâmetro: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }
}
