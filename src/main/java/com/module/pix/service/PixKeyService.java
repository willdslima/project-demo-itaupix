package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.exception.ResourceNotFoundException;
import com.module.pix.exception.UnprocessableEntityException;
import com.module.pix.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;
    private final PixKeyValidationService validationService;

    public PixKeyResponseDTO create(PixKeyRequestDTO pixKeyRequestDTO) {
        log.info("Iniciando criação da chave PIX para conta/agência: {}/{}", pixKeyRequestDTO.getAccountNumber(), pixKeyRequestDTO.getAgencyNumber());
        validationService.validateForCreated(pixKeyRequestDTO);

        PixKeyEntity savedEntity = pixKeyRepository.save(PixKeyEntity.buildResponseEntity(pixKeyRequestDTO));
        log.info("Chave PIX criada com sucesso. ID: {}", savedEntity.getId());
        return PixKeyResponseDTO.buildResponsePixUUID(savedEntity.getId());
    }

    public PixKeyResponseDTO update(UUID id, PixKeyUpdateDTO pixKeyUpdateDTO) {
        log.info("Iniciando atualização da chave PIX com ID: {}", id);
        PixKeyEntity pixKeyEntity = validationService.validateForUpdate(id, pixKeyUpdateDTO);
        PixKeyEntity updated = PixKeyEntity.buildUpdateEntity(pixKeyUpdateDTO, pixKeyEntity);

        return PixKeyResponseDTO.buildResponseDTO(pixKeyRepository.save(updated));
    }

    public List<PixKeyResponseDTO> getPixKeys(PixKeySearchDTO pixKeySearchDTO) {
        log.info("Iniciando consulta de chaves PIX com filtros: {}", pixKeySearchDTO);
        Specification<PixKeyEntity> specification = validationService.validateFilterForSearch(pixKeySearchDTO);

        List<PixKeyEntity> pixKeys = pixKeyRepository.findAll(specification);

        if (pixKeys.isEmpty()) {
            log.warn("Nenhuma chave PIX encontrada com os filtros fornecidos.");
            throw new ResourceNotFoundException("Nada encontrado para os filtros informados.");
        }

        log.info("Consulta finalizada. Total de chaves encontradas: {}", pixKeys.size());
        return pixKeys.stream()
                .map(PixKeyResponseDTO::buildResponseDTO)
                .collect(Collectors.toList());
    }

}
