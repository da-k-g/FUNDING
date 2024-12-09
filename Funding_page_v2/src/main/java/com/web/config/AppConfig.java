package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class AppConfig {

    String apiKey = "7066613284703264";
    String secretKey = "MK17kAot8ys07vpyygvLlqi2lMTuHhwcttRW2SmFp0gBKQuM9kok19P6zXzQig7BVXlaCseysRHXHWFF";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}