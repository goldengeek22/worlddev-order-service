package net.worlddev.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * @author Alexandre AMEVOR
 */

@Component
public class BookWebClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn){
        return webClient.get()
                .uri(BOOKS_ROOT_API+isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3),Mono.empty())//Sets a 3-second timeout for the GET request and if expires, the fallback is to return an empty Mono object
                .onErrorResume(WebClientResponseException.NotFound.class,ex->Mono.empty()) // Returns an empty object when a 404 response is received
                .retryWhen(Retry.backoff(3,Duration.ofMillis(100))) // Exponential backoff is used as the retry strategy. Three attempts are allowed with a 100 ms initial backoff
                .onErrorResume(Exception.class,ex->Mono.empty());// If any error happens after the 3 retry attempts, catch the exception and return an empty object
    }
}
