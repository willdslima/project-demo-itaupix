package com.module.pix.service;

import com.module.pix.dto.PixKeyRequestDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.utils.PixKeyValidatorUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class PixService {
    public PixKeyEntity create (PixKeyRequestDTO pixKeyRequest) {
        //Validar o valor da cave, tipos cf, email etcs.
        //utils de validacao
        PixKeyValidatorUtils.validateValueByKeyType(pixKeyRequest.getKeyType(), pixKeyRequest.getKeyValue());


        //buscar chave ativa no banco
        //repositorios

        //definir limite maximo de chaves

        // verificar se exiete chave com esse valor para de todos correntistas

        // construir objeto  final para salvar


        return null;
    }
}
