package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;


@ExtendWith(MockitoExtension.class)  // Enable Mockito annotations
class BookControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;


    private static final BookDTO BOOK1 = new BookDTO(1L, "The Pragmatic Programmer", "Andy Hunt", 42.99);
    private static final BookDTO BOOK2 = new BookDTO(2L, "Clean Code", "Robert C. Martin", 38.50);

    // Helper method to initialize MockMvc before each test
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }
    //TestCase 1 : Test all books has been returned when trigger /api/v1/books
    @Test
    void givenBooks_whenGetAllBooks_thenReturnList() throws Exception {
        // Given: Setup mock behavior - bookService will return a predefined list of books when getAllBooks() is called
        when(bookService.getAllBooks()).thenReturn(List.of(BOOK1, BOOK2));

        // When: Perform an HTTP GET request on the /api/v1/books endpoint with JSON content type
        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then: Expect the HTTP response status to be 200 OK
                .andExpect(status().isOk())
                // Then: Expect the JSON response array length to be 2
                .andExpect(jsonPath("$.length()").value(2))
                // Then: Expect the first book’s title to be "The Pragmatic Programmer"
                .andExpect(jsonPath("$[0].title").value("The Pragmatic Programmer"))
                // Then: Expect the second book’s author to be "Robert C. Martin"
                .andExpect(jsonPath("$[1].author").value("Robert C. Martin"));

        // Verify that bookService.getAllBooks() was called exactly once during the test
        verify(bookService, times(1)).getAllBooks();
        // Verify that no other interactions with bookService happened beyond what was expected
        verifyNoMoreInteractions(bookService);
    }

}