package com.oneplat.oap.mgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oneplat.oap.core.validation.validator.UniqueKeysValidator;
import com.oneplat.oap.core.validation.validator.UniqueNameValidator;

@Configuration
public class ValidatorContext {

    @Bean
    public UniqueNameValidator uniqueNameValidator() {
        return new UniqueNameValidator();
    }
    
    @Bean
    public UniqueKeysValidator uniqueKeysValidator() {
        return new UniqueKeysValidator();
    }
}
