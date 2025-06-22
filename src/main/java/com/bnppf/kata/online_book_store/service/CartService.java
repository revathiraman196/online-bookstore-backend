package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.CartItemDto;

public interface CartService {
    //add item into the cart
     CartItemDto addToCart(Long bookId, int quantity) ;
     void removeFromCart(Long cartItemId);
}
