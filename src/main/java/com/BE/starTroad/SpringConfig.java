package com.BE.starTroad;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.repository.UserRepository;
import com.BE.starTroad.service.OauthService;
import com.BE.starTroad.service.UserService;
import com.BE.starTroad.service.social.GoogleOauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.swing.*;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;
    private final EntityManager em;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

/*
    @Bean
    public GoogleOauth googleOauth() {
        return new GoogleOauth();
    }*/
/*
    @Bean
    public OauthService oauthService() {return new OauthService(); }
*/
    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }
}
