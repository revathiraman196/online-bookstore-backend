package com.bnppf.kata.online_book_store.repository;

import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBook(Book book);
}
