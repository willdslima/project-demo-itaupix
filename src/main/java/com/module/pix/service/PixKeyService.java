package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.exception.ResourceNotFoundException;
import com.module.pix.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;
    private final PixKeyValidationService validationService;

    public PixKeyResponseDTO create(PixKeyRequestDTO pixKeyRequestDTO) {
        validationService.validateForCreated(pixKeyRequestDTO);

        PixKeyEntity savedEntity = pixKeyRepository.save(PixKeyEntity.buildResponseEntity(pixKeyRequestDTO));

        return PixKeyResponseDTO.buildResponsePixUUID(savedEntity.getId());
    }

    public PixKeyResponseDTO update(UUID id, PixKeyUpdateDTO pixKeyUpdateDTO) {
        PixKeyEntity pixKeyEntity = validationService.validateForUpdate(id, pixKeyUpdateDTO);

        PixKeyEntity updated = PixKeyEntity.buildUpdateEntity(pixKeyUpdateDTO, pixKeyEntity);

        return PixKeyResponseDTO.buildResponseDTO(pixKeyRepository.save(updated));
    }

    public List<PixKeyResponseDTO> getPixKeys(PixKeySearchDTO pixKeySearchDTO) {
        Specification<PixKeyEntity> specification = validationService.validateFilterForSearch(pixKeySearchDTO);

        List<PixKeyEntity> pixKeys = pixKeyRepository.findAll(specification);

        if (pixKeys.isEmpty()) {
            throw new ResourceNotFoundException("Nada encontrado para os filtros informados.");
        }

        return pixKeys.stream()
                .map(PixKeyResponseDTO::buildResponseDTO)
                .collect(Collectors.toList());
    }

}
