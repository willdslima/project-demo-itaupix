package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.repository.PixKeyRepository;
import com.module.pix.utils.PixKeyValidatorUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;

    public PixKeyEntity create(PixKeyRequestDTO request) {

        PixKeyValidatorUtils.validateValueByKeyType(request.getKeyType(), request.getKeyValue());

        List<PixKeyEntity> activeKeys = pixKeyRepository.findByAgencyNumberAndAccountNumberAndDeactivationDateIsNull(
                request.getAgencyNumber(), request.getAccountNumber());

        if (activeKeys.size() >= 5) {
            throw new ValidationException("Limite de chaves para esta conta foi atingido: " + 5);
        }

        boolean exists = pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull(request.getKeyValue());
        if (exists) {
            throw new ValidationException("Chave já existe para outro correntista.");
        }

        PixKeyEntity pixKey = PixKeyEntity.builder()
                .keyType(request.getKeyType())
                .keyValue(request.getKeyValue())
                .accountType(request.getAccountType())
                .agencyNumber(request.getAgencyNumber())
                .accountNumber(request.getAccountNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .build();

        return pixKeyRepository.save(pixKey);
    }

}
