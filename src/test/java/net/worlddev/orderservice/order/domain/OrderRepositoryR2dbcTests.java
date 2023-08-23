package net.worlddev.orderservice.order.domain;

import net.worlddev.orderservice.order.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

/**
 * @author Alexandre AMEVOR
 */

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryR2dbcTests {

    @Container
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres"));

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry){
        registry.add("spring.r2dbc.url",OrderRepositoryR2dbcTests::r2dbcUrl);
        registry.add("spring.r2dbc.username",dbContainer::getUsername);
        registry.add("spring.r2dbc.password",dbContainer::getPassword);
        registry.add("spring.flyway.url",dbContainer::getJdbcUrl);
    }

    private static String r2dbcUrl(){
        return String.format("r2dbc:postgresql://%s:%s/%s", dbContainer.getHost(),dbContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),dbContainer.getDatabaseName());
    }

    @Test
    void createRejectedOrder(){
        var rejectedOrder = OrderService.buildRejectedOrder("000000000",1);
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> order.status().equals(OrderStatus.REJECTED)).verifyComplete();
    }
}
