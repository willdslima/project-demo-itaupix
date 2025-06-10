package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.exception.ResourceNotFoundException;
import com.module.pix.repository.PixKeyRepository;
import com.module.pix.utils.PixKeyValidatorUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PixKeyValidationService {

    private final PixKeyRepository pixKeyRepository;

    public void validateForCreated(PixKeyRequestDTO pixKeyRequestDTO) {
        PixKeyValidatorUtils.validateValueByKeyType(pixKeyRequestDTO.getKeyType(), pixKeyRequestDTO.getKeyValue());

        validateDuplicateKey(pixKeyRequestDTO);
        validateAccountKeyLimit(pixKeyRequestDTO);
    }

    private void validateDuplicateKey(PixKeyRequestDTO pixKeyRequestDTO) {
        boolean exists = pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull(pixKeyRequestDTO.getKeyValue());
        if (exists) {
            throw new ValidationException("Chave já existe para outro correntista.");
        }
    }

    private void validateAccountKeyLimit(PixKeyRequestDTO pixKeyRequestDTO) {
        List<?> activeKeys = pixKeyRepository.findByAgencyNumberAndAccountNumberAndDeactivationDateIsNull(
                pixKeyRequestDTO.getAgencyNumber(), pixKeyRequestDTO.getAccountNumber());

        if (activeKeys.size() >= 5) {
            throw new ValidationException("Limite de chaves para esta conta foi atingido: 5");
        }
    }

    public PixKeyEntity validateForUpdate(UUID id, PixKeyUpdateDTO pixKeyUpdateDTO) {
        PixKeyEntity existing = pixKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chave PIX não encontrada"));

        if (!existing.isActive()) {
            throw new ValidationException("Chaves PIX inativadas não podem ser alteradas");
        }

        if (pixKeyUpdateDTO.getKeyType() != null || pixKeyUpdateDTO.getKeyValue() != null || pixKeyUpdateDTO.getId() != null) {
            throw new ValidationException("Campos 'ID', 'keyType' e 'keyValue' não podem ser alterados");
        }

        return existing;
    }

}
