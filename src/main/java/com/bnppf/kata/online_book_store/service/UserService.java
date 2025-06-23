package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.LoginRequest;
import com.bnppf.kata.online_book_store.dto.RegisterRequest;
import com.bnppf.kata.online_book_store.dto.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
}
