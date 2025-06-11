package com.module.pix.utils;

import com.module.pix.enums.KeyTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KeyTypeEnumConverter implements Converter<String, KeyTypeEnum> {

    @Override
    public KeyTypeEnum convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        return KeyTypeEnum.valueOf(source.trim().toUpperCase());
    }
}
