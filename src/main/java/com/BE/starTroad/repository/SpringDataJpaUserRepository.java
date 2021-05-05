package com.BE.starTroad.repository;

import com.BE.starTroad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;

@Resource
public interface SpringDataJpaRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
