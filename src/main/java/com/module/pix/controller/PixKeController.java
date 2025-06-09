package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pix")
public class PixKeController {
    @PostMapping
    public ResponseEntity<UUID> cadastrar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        return ResponseEntity.ok(null);
    }

}
