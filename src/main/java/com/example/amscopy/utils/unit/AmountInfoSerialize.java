package com.example.amscopy.utils.unit;


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
public class AmountInfoSerialize extends JsonSerializer<Object> implements ContextualSerializer {

    private AmountType type;
    private boolean roundHalfUp;
    private String numberFormat;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            case YUAN:
                gen.writeObject(value);
            case WAN_YUAN:
                gen.writeObject(AmountUtil.asWanYuanUnit(value, roundHalfUp, numberFormat));
            case NUMBER:
                gen.writeObject(AmountUtil.asFormatString(value, numberFormat));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)
                    || Objects.equals(property.getType().getRawClass(), BigDecimal.class)) {  //非String bigDecimal 类直接跳过
                AmountInfo amountInfo = property.getAnnotation(AmountInfo.class);
                if (amountInfo == null) {
                    amountInfo = property.getContextAnnotation(AmountInfo.class);
                }
                if (amountInfo != null) {
                    return new AmountInfoSerialize(amountInfo.value(), amountInfo.roundHalfUp(), amountInfo.numberFormat());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(property);
    }
}
