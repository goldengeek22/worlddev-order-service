package net.worlddev.orderservice.book;

/**
 * @author Alexandre AMEVOR
 */
public record Book(
        String isbn,
        String title,
        String author,
        Double price
) {
}
