package com.bnppf.kata.online_book_store.controller;

import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
