package com.BE.starTroad.service;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //회원 가입
    public String join(User user) {

        //validateDuplicateMember(user);
        userRepository.save(user);
        return user.getEmail();
    }

    /*private void validateDuplicateMember(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }*/
    //전체 회원 조회
    public List<User> findMembers() {

        return userRepository.findAll();

    }

    public Optional<User> findOne(String email) {
        return userRepository.findById(email);
    }
}