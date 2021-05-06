package com.BE.starTroad.service;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.repository.SpringDataJpaUserRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaUserServiceTest {

    @Autowired
    JpaUserService jpaUserService;
    @Autowired
    SpringDataJpaUserRepository springDataJpaUserRepository;

    @Test
    void 회원가입() {
        //given
        User user = new User();
        String email = "testemail@test.com";
        user.setName("testtest");
        user.setEmail(email);
        //when
        jpaUserService.save(user);
        //then
        Optional<User> findUser = springDataJpaUserRepository.findByEmail(email);
        assertEquals(user.getName(), findUser.get().getName());
    }

    @Test
    void 이메일로_사람_찾기() {

        String email = "joyslee1010@gmail.com";
        Optional<User> user = jpaUserService.findByEmail(email);

        if (user.isPresent()) {
            assertEquals(user.get().getEmail(),email);
        }
    }

    @Test
    void 업데이트() {

        String email = "joyslee1010@gmail.com";

        User updateUser = new User();

        updateUser.setMessage("test!!!");

        User user = jpaUserService.update(email,updateUser);

        assertEquals(user.getEmail(), email);

    }

}