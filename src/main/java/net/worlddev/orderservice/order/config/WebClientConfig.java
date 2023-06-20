package net.worlddev.orderservice.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alexandre AMEVOR
 */

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(ClientProperties clientProperties,WebClient.Builder webClientBuilder){
        return webClientBuilder.baseUrl(clientProperties.catalogServiceUri().toString()).build();
    }
}
