package lk.ac.mrt.fit.library_api.config;

import lk.ac.mrt.fit.library_api.model.Book;
import lk.ac.mrt.fit.library_api.repository.BookRepository;
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
                        new Book("Clean Code", "Robert C. Martin", "978-0132350884", 4500.0),
                        new Book("The Pragmatic Programmer", "David Thomas, Andrew Hunt", "978-0135957059", 5200.0),
                        new Book("Design Patterns", "Erich Gamma et al.", "978-0201633610", 6800.0),
                        new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084", 2100.0),
                        new Book("Atomic Habits", "James Clear", "978-0735211292", 3400.0)
                ));
            }
        };
    }
}

