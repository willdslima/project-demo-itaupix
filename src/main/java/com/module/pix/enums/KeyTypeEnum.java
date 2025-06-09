package com.module.pix.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum KeyTypeEnum {
    CELULAR,
    EMAIL,
    CPF,
    CNPJ,
    ALEATORIA;

    @JsonCreator
    public static KeyTypeEnum fromString(String valor) {
        for (KeyTypeEnum tipo : KeyTypeEnum.values()) {
            if (tipo.name().equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de chave inválido: " + valor);
    }

}
