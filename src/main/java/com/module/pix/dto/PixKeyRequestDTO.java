package com.module.pix.dto;

import com.module.pix.enums.KeyTypeEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PixKeyRequestDTO {

    @NotNull(message = "Tipo da chave deve ser informado")
    private KeyTypeEnum keyType;

    @NotBlank(message = "Valor da chave deve ser informado")
    private String keyValue;

    @NotNull(message = "Tipo da conta deve ser informado")
    @Pattern(regexp = "corrente|poupanca", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Tipo de conta deve ser 'corrente' ou 'poupanca'"
    )
    private String accountType;

    @NotNull(message = "Número da agência deve ser informado")
    @Digits(integer = 4, fraction = 0, message = "Número da agência deve conter até 4 dígitos numéricos")
    private Integer agencyNumber;

    @NotNull(message = "Número da conta deve ser informado")
    @Digits(integer = 8, fraction = 0, message = "Número da conta deve conter até 8 dígitos numéricos")
    private Integer accountNumber;

    @NotBlank(message = "Nome do correntista deve ser informado")
    private String firstName;

    private String lastName;
}
