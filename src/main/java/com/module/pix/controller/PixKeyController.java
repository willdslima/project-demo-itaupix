package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.dto.PixKeyResponseDTO;
import com.module.pix.dto.PixKeySearchDTO;
import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.service.PixKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/search")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeys(@Valid PixKeySearchDTO pixKeySearchDTO) {
        return ResponseEntity.ok(pixKeyService.getPixKeys(pixKeySearchDTO));
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
