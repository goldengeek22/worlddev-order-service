package net.worlddev.orderservice.order.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Alexandre AMEVOR
 */
public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
}
