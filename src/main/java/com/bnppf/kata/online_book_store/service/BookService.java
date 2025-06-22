package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.BookDTO;

import java.util.List;

public interface BookService {
    // Fetch all books from the store
    List<BookDTO> getAllBooks();
}
