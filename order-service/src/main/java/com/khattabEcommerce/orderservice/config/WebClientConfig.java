package com.khattabEcommerce.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    // add client side load balanced capabilities to  the web client builder
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();

    }
}
