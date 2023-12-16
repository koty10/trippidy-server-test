package dev.koten.trippidy.serializers;

import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import java.math.BigDecimal;

public class BigDecimalSerializer implements JsonbSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal obj, JsonGenerator generator, SerializationContext ctx) {
        generator.write(obj.toString());
    }
}