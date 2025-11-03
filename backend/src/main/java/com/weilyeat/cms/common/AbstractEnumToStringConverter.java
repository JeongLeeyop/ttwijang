package com.weilyeat.cms.common;

import javax.persistence.AttributeConverter;

public abstract class AbstractEnumToStringConverter<E extends Enum<E>> implements AttributeConverter<E, String> {
    protected abstract Class<E> getEnumType();

    @Override
    public E convertToEntityAttribute(String dbData) {
        try {
            return Enum.valueOf(getEnumType(), dbData.trim().toUpperCase());
        } catch (Throwable e) {
            // ignore
        }
        return null;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (attribute != null) {
            return attribute.name();
        }
        return null;
    }
}
