package com.blessing.lentaldream.infra.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

//    @Bean("errorCode")
//    public PropertiesFactoryBean propertiesFactoryBean() throws  Exception{
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        ClassPathResource classPathResource = new ClassPathResource("messages.properties");
//        propertiesFactoryBean.setLocation(classPathResource);
//        return propertiesFactoryBean;
//    }
}
