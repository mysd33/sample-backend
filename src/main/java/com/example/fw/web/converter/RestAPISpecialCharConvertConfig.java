package com.example.fw.web.converter;

import java.io.IOException;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.utils.JapaneseStringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * REST API用の特殊文字のコードポイント変換の設定クラス
 */
@Configuration
public class RestAPISpecialCharConvertConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.deserializerByType(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return JapaneseStringUtils.exchageSpecialChar(value);
            }
        });
    }

}
