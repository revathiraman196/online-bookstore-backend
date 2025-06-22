package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {
    private final CartService cartService;
    /**
     * Adds a book to the cart with a given quantity.
     *
     * @param bookId   the ID of the book to add
     * @param quantity the quantity to add
     * @return CartItemDto representing the added/updated cart item
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity) {
        log.info("Adding to cart...");
        CartItemDto addedItem = cartService.addToCart(bookId, quantity);
        return ResponseEntity.ok(addedItem);
    }
    /**
     * Deletes a cart item by its ID.
     *
     * @param cartItemId the ID of the cart item to delete
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> deleteCartItemById(@PathVariable Long cartItemId) {
        log.info("Request received to delete cart item with ID: {}", cartItemId);

        cartService.removeFromCart(cartItemId);

        log.info("Cart item with ID: {} deleted successfully", cartItemId);
        return ResponseEntity.ok("Cart item deleted successfully");
    }
    /**
     * Updates the quantity of a cart item identified by its ID.
     *
     * @param cartItemId the ID of the cart item to update
     * @param quantity   the new quantity to set
     * @return ResponseEntity with updated CartItemDto and HTTP 200 status
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemDto> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {

        log.info("Request received to update quantity of cart item ID {} to {}", cartItemId, quantity);

        // Delegate the update operation to the service layer
        CartItemDto updatedItem = cartService.updateCartItemQuantity(cartItemId, quantity);

        // Return the updated cart item DTO with HTTP 200 OK status
        return ResponseEntity.ok(updatedItem);
    }

}
