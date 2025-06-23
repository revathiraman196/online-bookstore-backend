package com.bnppf.kata.online_book_store.utility;

import com.bnppf.kata.online_book_store.dto.RegisterRequest;
import com.bnppf.kata.online_book_store.dto.UserResponse;
import com.bnppf.kata.online_book_store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Map RegisterRequest to User entity but ignore passwordHash field (we'll set hashed password manually)
    @Mapping(target = "password", ignore = true)
    User registerRequestToUser(RegisterRequest request);

    // Map User entity to UserResponse DTO
    UserResponse userToUserResponse(User user);
}
