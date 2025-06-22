package com.bnppf.kata.online_book_store.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.entity.CartItem;
import com.bnppf.kata.online_book_store.exception.DataNotFoundException;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import com.bnppf.kata.online_book_store.repository.CartItemRepository;
import com.bnppf.kata.online_book_store.utility.CartItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    // Mocked dependencies used by CartServiceImpl
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    // The service under test, with mocks automatically injected
    @InjectMocks
    private CartServiceImpl cartService;
    private Book book1; //Book entity class

    /**
     * Test case: Book exists and is successfully added to the cart.*
     * Scenario:
     * - A book with the specified ID exists in the repository.
     * - No existing CartItem is found for this book, so a new CartItem is created.
     * - The quantity is added, saved, and converted to a DTO.*
     * Expected:
     * - The returned CartItemDto reflects the correct quantity.
     * - The save method is invoked once.
     */
    @Test
    void shouldAddBookToCart_WhenBookExists() {

        Long bookId = 1L;
        int quantity = 2;

        book1 = new Book(1L, "The Pragmatic Programmer", "Andy Hunt", 42.99);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book1);
        cartItem.setQuantity(0);

        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(100L);
        savedCartItem.setBook(book1);
        savedCartItem.setQuantity(2);

        CartItemDto expectedDto = new CartItemDto();
        expectedDto.setId(100L);
        expectedDto.setQuantity(2);

        // Define mock behavior
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
        when(cartItemRepository.findByBook(book1)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedCartItem);
        when(cartItemMapper.toResponseDTO(savedCartItem)).thenReturn(expectedDto);

        // Act
        CartItemDto result = cartService.addToCart(bookId, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    /**
     * Test case: Book is not found in the repository.*
     * Scenario:
     * - The repository returns an empty Optional for the given book ID.*
     * Expected:
     * - A DataNotFoundException is thrown.
     * - No cart item is saved.
     * - The mapper is never called.
     */
    @Test
    void shouldThrowException_WhenBookDoesNotExist() {
        // Arrange
        Long bookId = 999L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> cartService.addToCart(bookId, 1));

        // Verify no interaction with repository save or mapper
        verify(cartItemRepository, never()).save(any());
        verify(cartItemMapper, never()).toResponseDTO(any());
    }
}

