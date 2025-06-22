package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.exception.DataNotFoundException;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import com.bnppf.kata.online_book_store.utility.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

            log.info("Getting all books");
            // Retrieve all books from the repository
            List<Book> books = bookRepository.findAll();
            // Handle case where no books are found (optional)
            if (books.isEmpty()) {
                throw new DataNotFoundException("No books found");
            }
            log.debug("Found {} books", books.size());
            // Convert the list of Book entities to BookDTOs
            return books.stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

    }
}
