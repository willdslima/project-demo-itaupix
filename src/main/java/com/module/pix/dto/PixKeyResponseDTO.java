package com.module.pix.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class PixKeyResponseDTO {
    
    private UUID pixKey;
    private KeyTypeEnum keyType;
    private String keyValue;
    private String accountType;
    private Integer agencyNumber;
    private Integer accountNumber;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PixKeyResponseDTO buildResponsePixUUID(UUID pixKey) {
        return PixKeyResponseDTO.builder()
                .pixKey(pixKey)
                .build();
    }

}
