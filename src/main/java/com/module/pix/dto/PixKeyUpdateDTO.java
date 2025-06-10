package com.module.pix.dto;

import com.module.pix.enums.KeyTypeEnum;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PixKeyUpdateDTO {

    private UUID id;
    private KeyTypeEnum keyType;
    private String keyValue;

    @Pattern(regexp = "corrente|poupanca", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Tipo de conta deve ser 'corrente' ou 'poupanca'"
    )
    private String accountType;

    @Digits(integer = 4, fraction = 0, message = "Número da agência deve conter até 4 dígitos numéricos")
    private Integer agencyNumber;

    @Digits(integer = 8, fraction = 0, message = "Número da conta deve conter até 8 dígitos numéricos")
    private Integer accountNumber;

    private String firstName;

    private String lastName;
}
