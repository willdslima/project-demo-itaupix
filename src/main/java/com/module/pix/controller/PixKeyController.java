package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.service.PixKeyService;
import com.module.pix.validation.Create;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/pix")
public class PixKeyController {

    private final PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<PixKeyResponseDTO> createPixKey(@Validated(Create.class) @Valid @RequestBody PixKeyRequestDTO pixKeyRequestDTO) {
        return ResponseEntity.ok(pixKeyService.create(pixKeyRequestDTO));
    }

    @GetMapping
    public ResponseEntity<PixKeyResponseDTO> pesquisar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // Consultar por id?
        //      yes, busca por id (if else de encontrado ou dados
        //      no, filtros validos? (case )
        //           no-> response filtros invalidos ou combinados
        //           yes-> busca por filtros
        //      busca: nok nao encontrado / ok retorna lista de chaves

        return ResponseEntity.ok(PixKeyResponseDTO.builder().build());
    }

    @PutMapping("/atualizar")
    public ResponseEntity<PixKeyResponseDTO> atualizar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        //busca por id
        //  desativada (chave inativa) / nao contrada (chave nao encontrada)
        //  encontrada -> validar campos alteraves, NOK campos invalidos
        //      atualizar dados permitidos
        //      response alteracao realizada
        return ResponseEntity.ok(PixKeyResponseDTO.builder().build());
    }

    @PutMapping("/inativar")
    public ResponseEntity<PixKeyResponseDTO> deletar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // busca por id
        //      Nao contrada(id nao encontrada) / Ja ativa(jave ja invativada)
        // Ativa (marca como inativa + data/hora da inativacao

        return ResponseEntity.ok(PixKeyResponseDTO.builder().build());
    }


}
