package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.service.PixKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pix")
public class PixKeyController {

    private final PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<PixKeyResponseDTO> createPixKey(@Valid @RequestBody PixKeyRequestDTO pixKeyRequestDTO) {
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


    @PutMapping("/{id}")
    public ResponseEntity<PixKeyResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody PixKeyUpdateDTO updateDTO) {
        return ResponseEntity.ok(pixKeyService.update(id, updateDTO));
    }


    @PutMapping("/inativar")
    public ResponseEntity<PixKeyResponseDTO> deletar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // busca por id
        //      Nao contrada(id nao encontrada) / Ja ativa(jave ja invativada)
        // Ativa (marca como inativa + data/hora da inativacao

        return ResponseEntity.ok(PixKeyResponseDTO.builder().build());
    }


}
