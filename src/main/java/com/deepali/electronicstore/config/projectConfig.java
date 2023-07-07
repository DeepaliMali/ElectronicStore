package com.deepali.electronicstore.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class projectConfig {

    @Bean
    public ModelMapper mapper()
    {
        return new ModelMapper();
    }
}
