package com.module.pix.entity;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.enums.KeyTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "key_px")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PixKeyEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 9, nullable = false)
    private KeyTypeEnum keyType;

    @Column(length = 77, nullable = false, unique = true)
    private String keyValue;

    @Column(length = 10, nullable = false)
    private String accountType;

    @Column(length = 4, nullable = false)
    private Integer agencyNumber;

    @Column(length = 8, nullable = false)
    private Integer accountNumber;

    @Column(length = 30, nullable = false)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deactivationDate;


    public boolean isActive() {
        return deactivationDate == null;
    }

    public static PixKeyEntity buildResponseEntity(PixKeyRequestDTO pixKeyRequestDTO) {
        return PixKeyEntity.builder()
                .keyType(pixKeyRequestDTO.getKeyType())
                .keyValue(pixKeyRequestDTO.getKeyValue())
                .accountType(pixKeyRequestDTO.getAccountType())
                .agencyNumber(pixKeyRequestDTO.getAgencyNumber())
                .accountNumber(pixKeyRequestDTO.getAccountNumber())
                .firstName(pixKeyRequestDTO.getFirstName())
                .lastName(pixKeyRequestDTO.getLastName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PixKeyEntity buildUpdateEntity(PixKeyUpdateDTO dto, PixKeyEntity entity) {
        return entity.toBuilder()
                .accountType(dto.getAccountType() != null ? dto.getAccountType().toLowerCase() : entity.getAccountType())
                .agencyNumber(dto.getAgencyNumber() != null ? dto.getAgencyNumber() : entity.getAgencyNumber())
                .accountNumber(dto.getAccountNumber() != null ? dto.getAccountNumber() : entity.getAccountNumber())
                .firstName(dto.getFirstName() != null ? dto.getFirstName() : entity.getFirstName())
                .lastName(dto.getLastName() != null ? dto.getLastName() : entity.getLastName())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
