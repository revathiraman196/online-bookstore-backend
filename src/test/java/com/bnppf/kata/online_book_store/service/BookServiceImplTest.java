package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import com.bnppf.kata.online_book_store.utility.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Automatically initializes mocks for JUnit 5
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;  // The service being tested

    @Mock
    private BookRepository bookRepository;  // The dependency (repository)

    @Mock
    private BookMapper bookMapper;  // The BookMapper to convert between DTOs and Entities


    private Book book1; //Book entity class
    private Book book2;
    private BookDTO bookDTO1; //Book DTO class
    private BookDTO bookDTO2; //Book DTO class


    @BeforeEach
    void setUp() {
        // Creating sample books
        book1 = new Book(1L, "The Pragmatic Programmer", "Andy Hunt", 42.99);
        book2 = new Book(2L, "Clean Code", "Robert C. Martin", 38.50);
        // Creating corresponding BookDTOs
        bookDTO1 = new BookDTO(1L, "The Pragmatic Programmer", "Andy Hunt", 42.99);
        bookDTO2 = new BookDTO(2L, "Clean Code", "Robert C. Martin", 38.50);
    }

    // Test for getAllBooks
    @Test
    void testGetAllBooks() {
        // Setup mock behavior
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        // Call the method and verify results
        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());  // Ensure there are 2 books
        assertTrue(result.contains(bookDTO1));
        assertTrue(result.contains(bookDTO2));

        // Verify if the repository method was called once
        verify(bookRepository, times(1)).findAll();
    }

}