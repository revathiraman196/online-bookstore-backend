package com.bnppf.kata.online_book_store.service;

import com.bnppf.kata.online_book_store.dto.RegisterRequest;
import com.bnppf.kata.online_book_store.dto.UserResponse;
import com.bnppf.kata.online_book_store.entity.User;
import com.bnppf.kata.online_book_store.exception.UserAlreadyExistsException;
import com.bnppf.kata.online_book_store.repository.UserRepository;
import com.bnppf.kata.online_book_store.utility.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = userMapper.registerRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        return userMapper.userToUserResponse(user);
    }
}
