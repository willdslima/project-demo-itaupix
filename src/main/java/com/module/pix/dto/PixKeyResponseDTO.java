package com.module.pix.dto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deactivationDate;

    public static PixKeyResponseDTO buildResponsePixUUID(UUID id) {
        return PixKeyResponseDTO.builder()
                .id(id)
                .build();
    }

    public static PixKeyResponseDTO buildResponseDTO(PixKeyEntity entity) {
        return PixKeyResponseDTO.builder()
                .id(entity.getId())
                .keyType(entity.getKeyType())
                .keyValue(entity.getKeyValue())
                .accountType(entity.getAccountType())
                .agencyNumber(entity.getAgencyNumber())
                .accountNumber(entity.getAccountNumber())
                .firstName(entity.getFirstName())
                .lastName(Optional.ofNullable(entity.getLastName()).orElse(""))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deactivationDate(entity.getDeactivationDate())
                .build();
    }
}
