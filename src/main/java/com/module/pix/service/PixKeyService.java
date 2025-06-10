package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

        return PixKeyResponseDTO.buildResponseUpdatedDTO(pixKeyRepository.save(updated));
    }

}
