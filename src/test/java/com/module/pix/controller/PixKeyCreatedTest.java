package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixKeyCreatedTest {

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
    void deveCriarChavePixCpfComSucesso() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CPF);
        request.setKeyValue("12345678909");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        when(pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull("12345678909")).thenReturn(false);
        when(pixKeyRepository.findByAgencyNumberAndAccountNumber(
                request.getAgencyNumber(),request.getAccountNumber())).thenReturn(List.of());

        when(pixKeyRepository.save(any())).thenAnswer(invocation -> {
            PixKeyEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        });

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.createPixKey(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PixKeyResponseDTO body = response.getBody();
        assertNotNull(body.getId());
    }

    @Test
    void deveCriarChavePixCelularComSucesso() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("+5511998765432");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Carlos");
        request.setLastName("Souza");

        when(pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull("+5511998765432")).thenReturn(false);
        when(pixKeyRepository.findByAgencyNumberAndAccountNumber(
                request.getAgencyNumber(), request.getAccountNumber())).thenReturn(List.of());

        when(pixKeyRepository.save(any())).thenAnswer(invocation -> {
            PixKeyEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        });

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.createPixKey(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PixKeyResponseDTO body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
    }

    @Test
    void deveCriarChavePixEmailComSucesso() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.EMAIL);
        request.setKeyValue("maria.santos@email.com");
        request.setAccountType("poupanca");
        request.setAgencyNumber(4321);
        request.setAccountNumber(87654321);
        request.setFirstName("Maria");
        request.setLastName("Santos");

        when(pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull("maria.santos@email.com")).thenReturn(false);
        when(pixKeyRepository.findByAgencyNumberAndAccountNumber(
                request.getAgencyNumber(), request.getAccountNumber())).thenReturn(List.of());

        when(pixKeyRepository.save(any())).thenAnswer(invocation -> {
            PixKeyEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        });

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.createPixKey(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PixKeyResponseDTO body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
    }

    @Test
    void deveRetornarErroAoCriarChavePixComCpfDeTamanhoInvalido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CPF);
        request.setKeyValue("12345678");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("CPF deve conter exatamente 11 dígitos numéricos", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixComCelularInvalido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("5511987654321");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("João");
        request.setLastName("Pereira");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Número celular deve iniciar com '+'", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixComEmailSemArroba() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.EMAIL);
        request.setKeyValue("ana.silva.gmail.com");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Email deve conter '@'", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixComEmailFormatoInvalido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.EMAIL);
        request.setKeyValue("ana@@gmail..com");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Email em formato inválido", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixTelefoneSemPrefixoMais() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("5511987654321");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Número celular deve iniciar com '+'", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixTelefoneComTamanhoInvalido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("+123");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Celular inválido. Esperado: código país + DDD + número (11 a 14 dígitos)", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixTelefoneComFinalInvalido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("+55119876543A");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Celular inválido. Esperado: código país + DDD + número (11 a 14 dígitos)", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixDuplicada() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CPF);
        request.setKeyValue("12345678909");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Ana");
        request.setLastName("Silva");

        when(pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull("12345678909")).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Chave já existe para outro correntista.", ex.getMessage());
    }

    @Test
    void deveRetornarErroAoCriarChavePixQuandoLimiteDeCincoChavesPorContaForAtingido() {
        PixKeyRequestDTO request = new PixKeyRequestDTO();
        request.setKeyType(KeyTypeEnum.CELULAR);
        request.setKeyValue("+5511987654321");
        request.setAccountType("corrente");
        request.setAgencyNumber(1234);
        request.setAccountNumber(56781234);
        request.setFirstName("Carlos");
        request.setLastName("Souza");

        when(pixKeyRepository.existsByKeyValueAndDeactivationDateIsNull(request.getKeyValue())).thenReturn(false);

        List<PixKeyEntity> existingKeys = IntStream.range(0, 5)
                .mapToObj(i -> PixKeyEntity.builder()
                        .keyType(KeyTypeEnum.EMAIL)
                        .keyValue("email" + i + "@teste.com")
                        .accountType("corrente")
                        .agencyNumber(request.getAgencyNumber())
                        .accountNumber(request.getAccountNumber())
                        .firstName("Carlos")
                        .lastName("Souza")
                        .createdAt(LocalDateTime.now().minusDays(i))
                        .build())
                .collect(Collectors.toList());

        when(pixKeyRepository.findByAgencyNumberAndAccountNumber(
                request.getAgencyNumber(), request.getAccountNumber()))
                .thenReturn(existingKeys);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            pixKeyController.createPixKey(request);
        });

        assertEquals("Limite de chaves para esta conta foi atingido: 5", ex.getMessage());
    }

}
