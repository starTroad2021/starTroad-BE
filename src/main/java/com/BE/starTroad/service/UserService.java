package com.BE.starTroad.service;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//not in use
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
    //이메일로 사람 찾기

    //추가정보 입력

    /*public User updateUser(User user) {
        return userRepository.update(user);
    }*/
}