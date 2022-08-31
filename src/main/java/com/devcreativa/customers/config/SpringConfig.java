package com.devcreativa.customers.config;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    /*@Bean
    public Gson gson(){
        return new Gson();
    }*/

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
