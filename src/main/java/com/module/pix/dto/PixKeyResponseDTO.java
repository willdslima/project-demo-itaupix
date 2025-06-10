package com.module.pix.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;

import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class PixKeyResponseDTO {

    private UUID id;
    private KeyTypeEnum keyType;
    private String keyValue;
    private String accountType;
    private Integer agencyNumber;
    private Integer accountNumber;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PixKeyResponseDTO buildResponsePixUUID(UUID id) {
        return PixKeyResponseDTO.builder()
                .id(id)
                .build();
    }

    public static PixKeyResponseDTO buildResponseUpdatedDTO(PixKeyEntity pixKeyEntity) {
        return PixKeyResponseDTO.builder()
                .id(pixKeyEntity.getId())
                .keyType(pixKeyEntity.getKeyType())
                .keyValue(pixKeyEntity.getKeyValue())
                .accountType(pixKeyEntity.getAccountType())
                .agencyNumber(pixKeyEntity.getAgencyNumber())
                .accountNumber(pixKeyEntity.getAccountNumber())
                .firstName(pixKeyEntity.getFirstName())
                .lastName(pixKeyEntity.getLastName() != null ? pixKeyEntity.getLastName() : "")
                .createdAt(pixKeyEntity.getCreatedAt())
                .updatedAt(pixKeyEntity.getUpdatedAt())
                .build();
    }

}
