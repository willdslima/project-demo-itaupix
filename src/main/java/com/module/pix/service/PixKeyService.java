package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.repository.PixKeyRepository;
import com.module.pix.validation.PixKeyValidationService;
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

}
