package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String search,
                                 @RequestParam(required = false) Boolean available) {
        if (search != null && !search.trim().isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    search, search, search
            );
        }
        if (available != null) {
            return bookRepository.findByAvailable(available);
        }
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDetails.getTitle());
                    existingBook.setAuthor(bookDetails.getAuthor());
                    existingBook.setIsbn(bookDetails.getIsbn());
                    existingBook.setCategory(bookDetails.getCategory());
                    existingBook.setDescription(bookDetails.getDescription());
                    existingBook.setAvailable(bookDetails.isAvailable());
                    existingBook.setBorrowedBy(bookDetails.getBorrowedBy());
                    Book updated = bookRepository.save(existingBook);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long id, @RequestBody(required = false) Map<String, String> payload) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Book book = optionalBook.get();
        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Book is already borrowed"));
        }
        String borrower = (payload != null && payload.containsKey("borrower") && !payload.get("borrower").isBlank())
                ? payload.get("borrower")
                : "Library Member";
        book.setAvailable(false);
        book.setBorrowedBy(borrower);
        Book updated = bookRepository.save(book);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Book book = optionalBook.get();
        book.setAvailable(true);
        book.setBorrowedBy(null);
        Book updated = bookRepository.save(book);
        return ResponseEntity.ok(updated);
    }
}

