package com.BE.starTroad.repository;

import com.BE.starTroad.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String email);
    //Optional<User> findByName(String name);
    List<User> findAll();
}
