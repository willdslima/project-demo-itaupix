package com.module.pix.utils;

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
    private static final String EMAIL_PATTERN = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$";

    private static final int CPF_LENGTH = 11;

    public static void validateValueByKeyType(KeyTypeEnum keyType, String value) {
        switch (keyType) {
            case CELULAR -> validateCellPhone(value);
            case EMAIL -> validateEmail(value);
            case CPF -> validateCPF(value);
            default -> throw new ValidationException(ERROR_KEY_TYPE_INVALID);
        }
    }

    private static void validateCellPhone(String value) {
        if (!startsWithPlus(value)) throw new ValidationException(ERROR_STARTS_WITH_PLUS);

        String digits = value.substring(1);
        if (!isValidLength(digits, PHONE_MIN_LENGTH, PHONE_MAX_LENGTH)) throw new ValidationException(ERROR_INVALID_LENGTH);

        String numberPart = digits.substring(digits.length() - PHONE_NUMBER_PART_LENGTH);
        if (!isExactLength(numberPart, PHONE_NUMBER_PART_LENGTH)) throw new ValidationException(ERROR_NUMBER_PART_LENGTH);
    }

    private static void validateEmail(String value) {
        if (!containsAt(value)) throw new ValidationException(ERROR_EMAIL_MISSING_AT);
        if (isTooLong(value, EMAIL_MAX_LENGTH)) throw new ValidationException(ERROR_EMAIL_TOO_LONG);
        if (!matchesPattern(value, EMAIL_PATTERN)) throw new ValidationException(ERROR_EMAIL_INVALID_FORMAT);
    }

    private static void validateCPF(String value) {
        if (!isExactLength(value, CPF_LENGTH) || !isNumeric(value)) throw new ValidationException(ERROR_CPF_INVALID_LENGTH);
        if (!isValidCPF(value)) throw new ValidationException(ERROR_CPF_INVALID);
    }

    private static boolean startsWithPlus(String value) {
        return value != null && value.startsWith("+");
    }

    private static boolean isValidLength(String value, int min, int max) {
        return value != null && value.matches("\\d{" + min + "," + max + "}");
    }

    private static boolean isExactLength(String value, int length) {
        return value != null && value.length() == length && value.matches("\\d{" + length + "}");
    }

    private static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }

    private static boolean containsAt(String value) {
        return value != null && value.contains("@");
    }

    private static boolean isTooLong(String value, int maxLength) {
        return value != null && value.length() > maxLength;
    }

    private static boolean matchesPattern(String value, String regex) {
        return value != null && value.matches(regex);
    }

    public static boolean isValidCPF(String cpf) {
        // TODO validador de CPF EXTERNO
        return true;
    }
}
