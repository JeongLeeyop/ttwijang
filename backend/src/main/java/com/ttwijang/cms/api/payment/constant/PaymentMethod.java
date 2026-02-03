package com.ttwijang.cms.api.payment.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttwijang.cms.common.AbstractEnumToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
public enum PaymentMethod {
    CREDIT_CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    ;

    @Getter
    private final String label;

    public String getName() {
        return name();
    }

    @Nullable
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentMethod parse(@Nullable String name) {
        if (name != null) {
            try {
                return PaymentMethod.valueOf(name.trim().toUpperCase());
            } catch (Throwable e) {
                // ignore
            }
        }
        return null;
    }

    public static final class SingleConverter extends AbstractEnumToStringConverter<PaymentMethod> {
        @Override
        protected Class<PaymentMethod> getEnumType() {
            return PaymentMethod.class;
        }
    }

    public static final class MultipleConverter implements AttributeConverter<Set<PaymentMethod>, String> {
        @Override
        public String convertToDatabaseColumn(Set<PaymentMethod> attribute) {
            if (CollectionUtils.isEmpty(attribute)) {
                return "";
            }
            return attribute.stream()
                .map(PaymentMethod::name)
                .collect(Collectors.joining(","));
        }

        @Override
        public Set<PaymentMethod> convertToEntityAttribute(String dbData) {
            if (StringUtils.hasText(dbData)) {
                return Stream.of(dbData.split(","))
                    .map(String::trim)
                    .map(PaymentMethod::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()); // must be modifiable!
            }
            return new HashSet<>();
        }
    }
}
