package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.exception.DataNotFoundException;
import com.bnppf.kata.online_book_store.exception.GlobalExceptionHandler;
import com.bnppf.kata.online_book_store.service.CartService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Optional: your @ControllerAdvice for exceptions
                .build();
    }

    @Test
    void addToCart_ShouldReturnCartItemDto_WhenSuccessful() throws Exception {
        // Arrange
        Long bookId = 1L;
        int quantity = 2;

        CartItemDto dto = new CartItemDto();
        dto.setId(100L);
        dto.setQuantity(quantity);

        when(cartService.addToCart(bookId, quantity)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/cart/add")
                        .param("bookId", String.valueOf(bookId))
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }

    @Test
    void addToCart_ShouldReturn404_WhenBookNotFound() throws Exception {
        // Arrange
        Long bookId = 999L;
        int quantity = 1;

        when(cartService.addToCart(bookId, quantity))
                .thenThrow(new DataNotFoundException("Book not found"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/cart/add")
                        .param("bookId", String.valueOf(bookId))
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}