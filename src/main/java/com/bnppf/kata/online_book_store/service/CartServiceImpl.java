package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.entity.CartItem;
import com.bnppf.kata.online_book_store.exception.DataNotFoundException;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import com.bnppf.kata.online_book_store.repository.CartItemRepository;
import com.bnppf.kata.online_book_store.utility.CartItemMapper;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    public CartServiceImpl(BookRepository bookRepository, CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    // Adds a book to the shopping cart
    @Override
    public CartItemDto addToCart(Long bookId, int quantity) {
        // Try to find the book in the repository by its ID
        // If the book is not found, an exception is thrown
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new DataNotFoundException("Book not found"));

        CartItem item = cartItemRepository.findByBook(book).orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(0);
            return newItem;
        });

        item.setQuantity(item.getQuantity() + quantity);


        // Save the CartItem to the cartItemRepository
        // This will persist the cart item in the database or in-memory storage
        CartItem saved = cartItemRepository.save(item);

        return  cartItemMapper.toResponseDTO(saved);
    }
    /**
     * Removes a cart item by its ID.
     *
     * @param cartItemId ID of the cart item to remove
     * @throws DataNotFoundException if the cart item does not exist
     */
    @Override
    public void removeFromCart(Long cartItemId) {
        boolean exists = cartItemRepository.existsById(cartItemId);
        if (!exists) {
            throw new DataNotFoundException("Cart item with ID " + cartItemId + " not found.");
        }
        cartItemRepository.deleteById(cartItemId);
    }
    /**
     * Updates the quantity of an existing cart item.
     *
     * @param cartItemId the ID of the cart item to update
     * @param quantity   the new quantity to set
     * @return updated CartItemDto after saving
     * @throws DataNotFoundException if cart item with given ID does not exist
     */
    @Override
    public CartItemDto updateCartItemQuantity(Long cartItemId, int quantity) {
        // Fetch cart item by ID or throw exception if not found
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item with ID " + cartItemId + " not found."));

        // Update the quantity field
        cartItem.setQuantity(quantity);

        // Save the updated cart item entity back to the repository (DB)
        CartItem updatedItem = cartItemRepository.save(cartItem);

        // Convert the updated entity to DTO to return
        return cartItemMapper.toResponseDTO(updatedItem);
    }
}
