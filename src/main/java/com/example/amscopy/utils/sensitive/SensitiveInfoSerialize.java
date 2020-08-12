package com.example.amscopy.utils.sensitive;

import com.example.amscopy.utils.unit.AmountInfoSerialize;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveType type;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        switch (this.type) {
            case MOBILE:
                gen.writeString(SensitiveUtil.mobile(value));
                break;
            case USCC:
                gen.writeString(SensitiveUtil.uscc(value));
                break;
            case PRODUCT_NAME:
                gen.writeString(SensitiveUtil.productName(value));
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)
                    || Objects.equals(property.getType().getRawClass(), BigDecimal.class)) {  //非String 类直接跳过
                SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = property.getContextAnnotation(SensitiveInfo.class);
                }
                if (sensitiveInfo != null) {
                    return new SensitiveInfoSerialize(sensitiveInfo.value());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(property);
    }
}
