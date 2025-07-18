package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor  // Lombok generates constructor for final fields
@Slf4j                   // Lombok for logging
public class BookController {

    private final BookService bookService;

    /**
     * Get all books
     * @return list of BookDTOs
     */
    @GetMapping("books")
    public ResponseEntity<?> getAllBooks() {
        log.info("Received request to fetch all books");
        List<BookDTO> books = bookService.getAllBooks(); // can throw exceptions
        return ResponseEntity.ok(books);
    }



}
