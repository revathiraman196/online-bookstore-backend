package com.bnppf.kata.online_book_store.utility;

import com.bnppf.kata.online_book_store.dto.BookDTO;
import com.bnppf.kata.online_book_store.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    // Entity to Dto
    BookDTO toDTO(Book book);
    //Dto to Entity
    Book toEntity(BookDTO bookDTO);
}
