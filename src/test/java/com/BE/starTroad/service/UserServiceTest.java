package com.BE.starTroad.service;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    void 회원가입() {
        //given
        User newUser = new User();
        newUser.setEmail("test@gmail.com");
        newUser.setName("Test");
        //when
        String saveId = userService.join(newUser);

        //then
    }



}