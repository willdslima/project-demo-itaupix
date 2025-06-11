package com.module.pix.utils;

import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.entity.PixKeyEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class PixKeySpecificationBuilder {

    public static Specification<PixKeyEntity> buildSpecificFilter(PixKeySearchDTO pixKeySearchDTO) {
        if (pixKeySearchDTO.getId() != null) {
            return (root, query, cb) -> cb.equal(root.get("id"), pixKeySearchDTO.getId());
        }

        return Specification
                .allOf(equal("keyType", pixKeySearchDTO.getKeyType()))
                .and(equal("agencyNumber", pixKeySearchDTO.getAgencyNumber()))
                .and(equal("accountNumber", pixKeySearchDTO.getAccountNumber()))
                .and(likeIgnoreCase("firstName", pixKeySearchDTO.getFirstName()))
                .and(likeDatePattern("createdAt", pixKeySearchDTO.getCreatedAt() != null ? pixKeySearchDTO.getCreatedAt() : null))
                .and(likeDatePattern("updatedAt", pixKeySearchDTO.getUpdatedAt() != null ? pixKeySearchDTO.getUpdatedAt() : null));
    }

    private static <T> Specification<PixKeyEntity> equal(String field, T value) {
        return (value == null) ? null :
                (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<PixKeyEntity> likeIgnoreCase(String field, String value) {
        return (value == null || value.isBlank()) ? null :
                (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<PixKeyEntity> likeDatePattern(String field, String pattern) {
        if (pattern == null || pattern.isBlank()) return null;

        String isoPattern = pattern;
        try {
            isoPattern = LocalDate.parse(pattern, DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
        } catch (DateTimeParseException ignored) {}

        String finalPattern = "%" + isoPattern + "%";
        return (root, query, cb) ->
                cb.like(cb.function("to_char", String.class, root.get(field), cb.literal("YYYY-MM-DD")), finalPattern);
    }

}
