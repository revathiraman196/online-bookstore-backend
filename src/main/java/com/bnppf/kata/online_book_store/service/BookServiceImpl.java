package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import com.bnppf.kata.online_book_store.utility.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    // Constructor-based dependency injection
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }
    /**
     * Fetch all books from the repository and convert them to BookDTOs.
     *
     * @return a list of all books in the bookstore as BookDTOs
     */
    @Override
    public List<BookDTO> getAllBooks() {
        // Retrieve all books from the repository
        List<Book> books = bookRepository.findAll();
        // Convert the list of Book entities to BookDTOs
        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }
}
