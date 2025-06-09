package com.module.pix.dto;

import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
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

    public static PixKeyResponseDTO buildPixUUID(PixKeyEntity pixKey) {
        return PixKeyResponseDTO.builder()
                .id(pixKey.getId())
                .build();
    }

}
