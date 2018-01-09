package se.woolrich.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;

public class JsonLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser jsonparser,
                                 DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String time = jsonparser.getText();
        try {
            String[] split = time.split(":");
            return LocalTime.of(Integer.valueOf(split[0]),Integer.valueOf(split[1]),Integer.valueOf(split[2]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
