package com.bnppf.kata.online_book_store.utility;


import com.bnppf.kata.online_book_store.dto.CartItemDto;
import com.bnppf.kata.online_book_store.entity.CartItem;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = { BookMapper.class })
public interface CartItemMapper {

    CartItemDto toResponseDTO(CartItem cartItem);

}
