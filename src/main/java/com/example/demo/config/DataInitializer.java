package com.example.demo.config;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                bookRepository.saveAll(List.of(
                        new Book("Clean Code", "Robert C. Martin", "978-0132350884", "Technology", "A Handbook of Agile Software Craftsmanship teaching principles of writing clear, maintainable code.", true),
                        new Book("The Pragmatic Programmer", "David Thomas, Andrew Hunt", "978-0135957059", "Technology", "Your journey to mastery - covering core best practices and modern programming philosophy.", true),
                        new Book("Design Patterns", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", "978-0201633610", "Computer Science", "Elements of Reusable Object-Oriented Software explaining classic architectural patterns.", false),
                        new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", "Classic Fiction", "A gripping novel about innocence, compassion, and courage in the American South.", true),
                        new Book("Atomic Habits", "James Clear", "978-0735211292", "Self-Help", "An easy and proven way to build good habits and break bad ones with tiny, compounding changes.", true)
                ));
                // Set borrower for the 3rd book
                Book third = bookRepository.findAll().get(2);
                third.setBorrowedBy("Kasun Perera");
                bookRepository.save(third);
            }
        };
    }
}

