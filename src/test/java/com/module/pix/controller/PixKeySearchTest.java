package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;
import com.module.pix.exception.ResourceNotFoundException;
import com.module.pix.repository.PixKeyRepository;
import com.module.pix.service.PixKeyService;
import com.module.pix.service.PixKeyValidationService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixKeySearchTest {

    @Mock
    PixKeyRepository pixKeyRepository;

    PixKeyController pixKeyController;

    @BeforeEach
    void setUp() {
        PixKeyValidationService pixKeyValidationService = new PixKeyValidationService(pixKeyRepository);
        PixKeyService pixKeyService = new PixKeyService(pixKeyRepository, pixKeyValidationService);
        pixKeyController = new PixKeyController(pixKeyService);
    }

    @Test
    void deveBuscarChavePixPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        PixKeySearchDTO searchDTO = new PixKeySearchDTO();
        searchDTO.setId(id);

        PixKeyEntity entity = PixKeyEntity.builder()
                .id(id)
                .keyType(KeyTypeEnum.EMAIL)
                .keyValue("ana@email.com")
                .accountType("corrente")
                .agencyNumber(1234)
                .accountNumber(56781234)
                .firstName("Ana")
                .lastName("Silva")
                .createdAt(LocalDateTime.now())
                .build();

        when(pixKeyRepository.findAll(Mockito.<Specification<PixKeyEntity>>any()))
                .thenReturn(Collections.singletonList(entity));

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeys(searchDTO);

        PixKeyResponseDTO pixKeyResponseDTO = response.getBody().get(0);

        assertEquals(id, pixKeyResponseDTO.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(KeyTypeEnum.EMAIL, pixKeyResponseDTO.getKeyType());
        assertEquals("ana@email.com", pixKeyResponseDTO.getKeyValue());
        assertEquals(id, response.getBody().get(0).getId());
    }

    @Test
    void deveRetornarErroQuandoIdEOutrosFiltrosSaoEnviados() {
        PixKeySearchDTO searchDTO = new PixKeySearchDTO();
        searchDTO.setId(UUID.randomUUID());
        searchDTO.setFirstName("Ana");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            pixKeyController.getPixKeys(searchDTO);
        });

        assertEquals("Quando o ID é informado, nenhum outro filtro pode ser usado.", exception.getMessage());
    }

    @Test
    void deveRetornarErroQuandoNenhumFiltroEhInformado() {
        PixKeySearchDTO searchDTO = new PixKeySearchDTO();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            pixKeyController.getPixKeys(searchDTO);
        });

        assertEquals("Pelo menos um filtro deve ser informado.", exception.getMessage());
    }

    @Test
    void deveRetornarErroQuandoCreatedAtEUpdatedAtSaoInformadosJuntos() {
        PixKeySearchDTO searchDTO = new PixKeySearchDTO();
        searchDTO.setCreatedAt("01-06-2025");
        searchDTO.setUpdatedAt("02-06-2025");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            pixKeyController.getPixKeys(searchDTO);
        });

        assertEquals("Não é permitido combinar Data de Inclusão e Data de Atualizacao.", exception.getMessage());
    }

    @Test
    void deveRetornarErroQuandoNenhumaChavePixEhEncontradaParaFiltrosValidos() {
        PixKeySearchDTO searchDTO = new PixKeySearchDTO();
        searchDTO.setFirstName("NomeQueNaoExiste");

        when(pixKeyRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pixKeyController.getPixKeys(searchDTO);
        });

        assertEquals("Nada encontrado para os filtros informados.", exception.getMessage());
    }

}
