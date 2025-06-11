package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.exception.ResourceNotFoundException;
import com.module.pix.repository.PixKeyRepository;
import com.module.pix.utils.PixKeySpecificationBuilder;
import com.module.pix.utils.PixKeyValidatorUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

        PixKeyValidatorUtils.validForUpdate(pixKeyUpdateDTO, existing);

        return existing;
    }

    public Specification<PixKeyEntity> validateFilterForSearch(PixKeySearchDTO filter) {
        boolean hasId = filter.getId() != null;
        boolean hasKeyType = filter.getKeyType() != null;
        boolean hasAgencyNumber = filter.getAgencyNumber() != null;
        boolean hasAccountNumber = filter.getAccountNumber() != null;
        boolean hasFirstName = filter.getFirstName() != null && !filter.getFirstName().isBlank();
        boolean hasInclusionDate = filter.getCreatedAt() != null;
        boolean hasUpdatedDate = filter.getUpdatedAt() != null;

        boolean hasAllFilters = hasKeyType || hasAgencyNumber || hasAccountNumber || hasFirstName || hasInclusionDate || hasUpdatedDate;

        if (hasId && hasAllFilters) {
            throw new ValidationException("Quando o ID é informado, nenhum outro filtro pode ser usado.");
        }

        if (!hasId && !hasAllFilters) {
            throw new ValidationException("Pelo menos um filtro deve ser informado.");
        }

        if (hasInclusionDate && hasUpdatedDate) {
            throw new ValidationException("Não é permitido combinar Data de Inclusão e Data de Atualizacao.");
        }


        return PixKeySpecificationBuilder.buildSpecificFilter(filter);
    }

}
