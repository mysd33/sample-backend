package com.example.fw.web.validation.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.fw.web.validation.factorybean.StrippingIndexBracketsLocalValidatorFactoryBean;

import lombok.RequiredArgsConstructor;

/**
 * RestController向けの入力チェック機能拡張の設定クラス
 */
@Configuration
@RequiredArgsConstructor
public class ValidatorConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;

    /**
     * LocalValidatorFactoryBeanを拡張したValidatorのBean定義
     */
    @Bean
    Validator strippingIndexBracketsLocalValidatorFactoryBean() {
        StrippingIndexBracketsLocalValidatorFactoryBean validator = new StrippingIndexBracketsLocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    @Override
    public Validator getValidator() {
        return strippingIndexBracketsLocalValidatorFactoryBean();
    }
}
