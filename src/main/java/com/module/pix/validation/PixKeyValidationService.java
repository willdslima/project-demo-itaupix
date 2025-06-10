package com.module.pix.validation;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.entity.PixKeyEntity;
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

    public void validateForUpdated(UUID id, PixKeyRequestDTO pixKeyRequestDTO, PixKeyEntity pixKeyEntity) {
        if (!pixKeyEntity.isActive()) {
            throw new ValidationException("Chave inativa não pode ser atualizada");
        }

        if (pixKeyEntity.getKeyType() == null && !pixKeyEntity.getKeyType().equals(pixKeyRequestDTO.getKeyType())) {
            throw new ValidationException("Tipo da chave não pode ser alterado");
        }

        if (pixKeyEntity.getKeyValue() == null && !pixKeyEntity.getKeyValue().equals(pixKeyRequestDTO.getKeyValue())) {
            throw new ValidationException("Valor da chave não pode ser alterado");
        }

        if (!pixKeyEntity.getId().equals(id)) {
            throw new ValidationException("ID da chave não pode ser alterado");
        }
    }

}
