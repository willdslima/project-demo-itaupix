package com.module.pix.dto;

import com.module.pix.enums.KeyTypeEnum;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PixKeySearchDTO {

    private UUID id;

    private KeyTypeEnum keyType;
    private Integer agencyNumber;
    private Integer accountNumber;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String updatedAt;
}
