package net.worlddev.orderservice.order.domain;

import net.worlddev.orderservice.book.Book;
import net.worlddev.orderservice.book.BookWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Alexandre AMEVOR
 */

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookWebClient bookServiceClient;

    public OrderService(OrderRepository orderRepository, BookWebClient bookServiceClient) {
        this.orderRepository = orderRepository;
        this.bookServiceClient = bookServiceClient;
    }

    public Flux<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity){
        return Order.of(bookIsbn,null,null,quantity,OrderStatus.REJECTED);
    }

    public static Order buildAcceptedOrder(Book book, int quantity){
        return Order.of(book.isbn(), book.title()+" - "+book.author(), book.price(), quantity,OrderStatus.ACCEPTED);
    }

    public Mono<Order> submitOrder(String bookIsbn, int quantity){
        return bookServiceClient.getBookByIsbn(bookIsbn).map(book -> buildAcceptedOrder(book,quantity)).defaultIfEmpty(buildRejectedOrder(bookIsbn,quantity)).flatMap(orderRepository::save);
    }
}
