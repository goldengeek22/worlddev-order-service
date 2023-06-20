package net.worlddev.orderservice.order.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

/**
 * @author Alexandre AMEVOR
 */
@ConfigurationProperties(prefix = "worlddev")
public record ClientProperties(
        @NotNull
        URI catalogServiceUri
) {
}
