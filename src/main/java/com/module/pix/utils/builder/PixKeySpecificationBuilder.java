package com.module.pix.utils.builder;

import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.entity.PixKeyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class PixKeySpecificationBuilder {

    public static Specification<PixKeyEntity> buildSpecificFilter(PixKeySearchDTO pixKeySearchDTO) {
        if (pixKeySearchDTO.getId() != null) {
            log.info("Filtro por ID informado: {}", pixKeySearchDTO.getId());
            return (root, query, cb) -> cb.equal(root.get("id"), pixKeySearchDTO.getId());
        }

        log.info("Construindo filtros com os seguintes parâmetros: {}", pixKeySearchDTO);
        return Specification
                .allOf(equal("keyType", pixKeySearchDTO.getKeyType()))
                .and(equal("agencyNumber", pixKeySearchDTO.getAgencyNumber()))
                .and(equal("accountNumber", pixKeySearchDTO.getAccountNumber()))
                .and(likeIgnoreCase("firstName", pixKeySearchDTO.getFirstName()))
                .and(likeDatePattern("createdAt", pixKeySearchDTO.getCreatedAt() != null ? pixKeySearchDTO.getCreatedAt() : null))
                .and(likeDatePattern("updatedAt", pixKeySearchDTO.getUpdatedAt() != null ? pixKeySearchDTO.getUpdatedAt() : null));

    }

    private static <T> Specification<PixKeyEntity> equal(String field, T value) {
        if (value == null) return null;
        log.info("Filtro aplicado: {} == {}", field, value);
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<PixKeyEntity> likeIgnoreCase(String field, String value) {
        if (value == null || value.isBlank()) return null;
        log.info("Filtro aplicado (like ignore case): {} contém '{}'", field, value);
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<PixKeyEntity> likeDatePattern(String field, String pattern) {
        if (pattern == null || pattern.isBlank()) return null;

        String isoPattern = pattern;
        try {
            isoPattern = LocalDate.parse(pattern, DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
        } catch (DateTimeParseException e) {
            log.warn("Formato de data inválido para o campo {}: '{}'. Esperado: dd-MM-yyyy", field, pattern);
        }

        String finalPattern = "%" + isoPattern + "%";
        log.info("Filtro aplicado (por data): {} like '{}'", field, finalPattern);
        return (root, query, cb) ->
                cb.like(cb.function("to_char", String.class, root.get(field), cb.literal("YYYY-MM-DD")), finalPattern);
    }
}
