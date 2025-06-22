package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.dto.ErrorResponse;
import com.bnppf.kata.online_book_store.exception.DataNotFoundException;
import com.bnppf.kata.online_book_store.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor  // Lombok generates constructor for final fields
@Slf4j                   // Lombok for logging
public class BookController {

    private final BookService bookService;

    /**
     * Get all books
     * @return list of BookDTOs
     */
    @GetMapping
    public ResponseEntity<?> getAllBooks(HttpServletRequest request) {
        log.info("Received request to fetch all books");
        try {
            List<BookDTO> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (DataNotFoundException ex) {
            log.warn("No content to return: {}", ex.getMessage());
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Unexpected error: ", ex);
            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Unexpected error occurred while fetching books",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



}
