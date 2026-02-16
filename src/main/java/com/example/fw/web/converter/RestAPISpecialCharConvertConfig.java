package com.example.fw.web.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fw.common.utils.JapaneseStringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.module.SimpleModule;

/**
 * REST API用の特殊文字のコードポイント変換の設定クラス
 */
@Configuration
public class RestAPISpecialCharConvertConfig {

    @Bean
    SimpleModule specialCharModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new SpecialCharDeserializer());
        return module;
    }

    public static class SpecialCharDeserializer extends ValueDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            String value = p.getValueAsString();
            return JapaneseStringUtils.exchageSpecialChar(value);
        }
    }

}
