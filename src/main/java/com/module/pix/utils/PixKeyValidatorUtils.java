package com.module.pix.utils;

import com.module.pix.dto.PixKeyUpdateDTO;
import com.module.pix.entity.PixKeyEntity;
import com.module.pix.enums.KeyTypeEnum;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PixKeyValidatorUtils {

    private static final String ERROR_STARTS_WITH_PLUS = "Número celular deve iniciar com '+'";
    private static final String ERROR_INVALID_LENGTH = "Celular inválido. Esperado: código país + DDD + número (11 a 14 dígitos)";
    private static final String ERROR_NUMBER_PART_LENGTH = "Número deve conter exatamente 9 dígitos";
    private static final String ERROR_EMAIL_MISSING_AT = "Email deve conter '@'";
    private static final String ERROR_EMAIL_TOO_LONG = "Email não pode exceder 77 caracteres";
    private static final String ERROR_EMAIL_INVALID_FORMAT = "Email em formato inválido";
    private static final String ERROR_CPF_INVALID_LENGTH = "CPF deve conter exatamente 11 dígitos numéricos";
    private static final String ERROR_CPF_INVALID = "CPF inválido";
    private static final String ERROR_KEY_TYPE_INVALID = "Tipo de chave inválido";

    private static final int PHONE_MIN_LENGTH = 11;
    private static final int PHONE_MAX_LENGTH = 14;
    private static final int PHONE_NUMBER_PART_LENGTH = 9;

    private static final int EMAIL_MAX_LENGTH = 77;
    private static final String EMAIL_REGEX = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$";

    private static final int CPF_LENGTH = 11;

    public static void validateValueByKeyType(KeyTypeEnum keyType, String value) {
        if (value == null) throw new ValidationException("Valor da chave não pode ser nulo");

        switch (keyType) {
            case CELULAR -> validationCell(value);
            case EMAIL -> validationMail(value);
            case CPF -> validationCPF(value);
            default -> throw new ValidationException(ERROR_KEY_TYPE_INVALID);
        }
    }

    private static void validationCPF(String value) {
        if (!value.matches("\\d{" + CPF_LENGTH + "}")) throw new ValidationException(ERROR_CPF_INVALID_LENGTH);
        if (!isValidCPF(value)) throw new ValidationException(ERROR_CPF_INVALID);
    }

    private static void validationMail(String value) {
        if (!value.contains("@")) throw new ValidationException(ERROR_EMAIL_MISSING_AT);
        if (value.length() > EMAIL_MAX_LENGTH) throw new ValidationException(ERROR_EMAIL_TOO_LONG);
        if (!value.matches(EMAIL_REGEX)) throw new ValidationException(ERROR_EMAIL_INVALID_FORMAT);
    }

    private static void validationCell(String value) {
        if (!value.startsWith("+")) throw new ValidationException(ERROR_STARTS_WITH_PLUS);

        String digits = value.substring(1);
        if (!digits.matches("\\d{" + PHONE_MIN_LENGTH + "," + PHONE_MAX_LENGTH + "}"))
            throw new ValidationException(ERROR_INVALID_LENGTH);

        String numberPart = digits.substring(digits.length() - PHONE_NUMBER_PART_LENGTH);
        if (!numberPart.matches("\\d{" + PHONE_NUMBER_PART_LENGTH + "}"))
            throw new ValidationException(ERROR_NUMBER_PART_LENGTH);
    }

    public static boolean isValidCPF(String cpf) {
        // TODO: Implementar validação real de CPF
        return true;
    }

    public static void validForUpdate(PixKeyUpdateDTO pixKeyUpdateDTO, PixKeyEntity existing) {
        if (!existing.isActive()) {
            throw new ValidationException("Chaves PIX inativadas não podem ser alteradas");
        }

        if (pixKeyUpdateDTO.getId() != null) {
            throw new ValidationException("Campo 'ID' não pode ser alterado");
        }

        if (pixKeyUpdateDTO.getKeyType() != null) {
            throw new ValidationException("Campo 'keyType' não pode ser alterado");
        }

        if (pixKeyUpdateDTO.getKeyValue() != null) {
            throw new ValidationException("Campo 'keyValue' não pode ser alterado");
        }
    }
}
