package com.module.pix.utils;

import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.entity.PixKeyEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PixKeySpecificationBuilder {

    public static Specification<PixKeyEntity> buildSpecificFilter(PixKeySearchDTO dto) {
        if (dto.getId() != null) {
            return(root, query, cb) -> cb.equal(root.get("id"), dto.getId());
        }

        return Specification.allOf(equal("keyType", dto.getKeyType()))
                .and(equal("agencyNumber", dto.getAgencyNumber()))
                .and(equal("accountNumber", dto.getAccountNumber()))
                .and(likeIgnoreCase("firstName", dto.getFirstName()))
                .and(equal("createdAt", dto.getCreatedAt()))
                .and(equal("updatedAt", dto.getUpdatedAt()))
                .and(equal("deactivationDate", dto.getDeactivationDate()));
    }

    private static <T> Specification<PixKeyEntity> equal(String field, T value) {
        return (value == null) ? null :
                (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<PixKeyEntity> likeIgnoreCase(String field, String value) {
        return (value == null || value.isBlank()) ? null :
                (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

}
