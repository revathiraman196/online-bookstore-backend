package com.bnppf.kata.online_book_store.utility;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    // Convert Book entity to BookDTO
    public BookDTO toDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
    }

    // Convert BookDTO to Book entity
    public Book toEntity(BookDTO bookDTO) {
        return new Book(bookDTO.getId(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());
    }
}
