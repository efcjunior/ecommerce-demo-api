package demo.ecommerce.infrastructure.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class JacksonConfig {

    @Bean
    public Module uuidModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(UUID.class, new JsonDeserializer<>() {
            @Override
            public UUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getText();
                if (value == null || value.isBlank()) return null;
                if (value.contains("-")) return UUID.fromString(value);
                return UUID.fromString(value.replaceFirst(
                        "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                        "$1-$2-$3-$4-$5"
                ));
            }
        });
        return module;
    }
}
