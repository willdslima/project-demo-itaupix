package com.module.pix.controller;

import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixKeyUpdatedTest {

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
    void deveAtualizarChavePixComSucesso() {
        UUID id = UUID.randomUUID();
        PixKeyEntity entityExistente = new PixKeyEntity();
        entityExistente.setId(id);
        entityExistente.setKeyType(KeyTypeEnum.EMAIL);
        entityExistente.setKeyValue("ana@email.com");
        entityExistente.setFirstName("Ana");
        entityExistente.setLastName("Silva");
        entityExistente.setAgencyNumber(1234);
        entityExistente.setAccountNumber(56781234);
        entityExistente.setAccountType("corrente");
        entityExistente.setDeactivationDate(null);

        PixKeyUpdateDTO updateDTO = new PixKeyUpdateDTO();
        updateDTO.setFirstName("Ana Paula");
        updateDTO.setLastName("Silva Lima");

        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(entityExistente));
        when(pixKeyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.update(id, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PixKeyResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(id, body.getId());
        assertEquals("Ana Paula", body.getFirstName());
        assertEquals("Silva Lima", body.getLastName());
    }

    @Test
    void deveFalharAoTentarAlterarKeyType() {
        UUID id = UUID.randomUUID();
        PixKeyEntity entityExistente = new PixKeyEntity();
        entityExistente.setId(id);
        entityExistente.setKeyType(KeyTypeEnum.EMAIL);
        entityExistente.setKeyValue("ana@email.com");

        PixKeyUpdateDTO updateDTO = new PixKeyUpdateDTO();
        updateDTO.setKeyType(KeyTypeEnum.CPF);

        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(entityExistente));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> pixKeyController.update(id, updateDTO));

        assertEquals("Campo 'keyType' não pode ser alterado", exception.getMessage());
    }

    @Test
    void deveFalharAoNaoEncontrarChaveParaAtualizacao() {
        UUID id = UUID.randomUUID();
        PixKeyUpdateDTO updateDTO = new PixKeyUpdateDTO();
        updateDTO.setFirstName("Nome Qualquer");

        when(pixKeyRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> pixKeyController.update(id, updateDTO));

        assertEquals("Chave PIX não encontrada", exception.getMessage());
    }

}
