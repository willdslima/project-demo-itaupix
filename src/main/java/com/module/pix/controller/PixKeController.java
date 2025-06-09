package com.module.pix.controller;

import com.module.pix.dto.PixKeyRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pix")
public class PixKeController {
    @PostMapping
    public ResponseEntity<UUID> cadastrar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // Validar tipo de chave
        // NOK campos, limite e se existe, "Erros distintos"
        // OK salvar dados complestos
        // response cadastro realizado
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<UUID> pesquisar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // Consultar por id?
        //      yes, busca por id (if else de encontrado ou dados
        //      no, filtros validos? (case )
        //           no-> response filtros invalidos ou combinados
        //           yes-> busca por filtros
        //      busca: nok nao encontrado / ok retorna lista de chaves

        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<UUID> atualizar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        //busca por id
        //  desativada (chave inativa) / nao contrada (chave nao encontrada)
        //  encontrada -> validar campos alteraves, NOK campos invalidos
        //      atualizar dados permitidos
        //      response alteracao realizada
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<UUID> deletar (@RequestBody PixKeyRequestDTO pixKeyRequestDTO){
        // busca por id
        //      Nao contrada(id nao encontrada) / Ja ativa(jave ja invativada)
        // Ativa (marca como inativa + data/hora da inativacao

        return ResponseEntity.ok(null);
    }


}
