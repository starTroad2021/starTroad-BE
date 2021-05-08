package com.BE.starTroad;

import com.BE.starTroad.config.JwtAuthenticationEntryPoint;
import com.BE.starTroad.config.JwtRequestFilter;
import com.BE.starTroad.config.JwtTokenUtil;
import com.BE.starTroad.config.WebSecurityConfig;
import com.BE.starTroad.helper.converter.SocialLoginTypeConverter;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.repository.UserRepository;
import com.BE.starTroad.service.JwtUserDetailsService;
import com.BE.starTroad.service.OauthService;
import com.BE.starTroad.service.UserService;
import com.BE.starTroad.service.social.GoogleOauth;
import com.BE.starTroad.service.social.SocialOauth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.*;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;
    private final EntityManager em;


    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;

    }

    /*@Bean
    public UserService userService() {
        return new UserService(userRepository());
    }
    */
    /*@Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }*/
/*
    @Bean
    public SocialLoginTypeConverter socialLoginTypeConverter() {
        return new SocialLoginTypeConverter();
    }
*/
    /*
    @Bean
    public GoogleOauth googleOauth() {
        return new GoogleOauth();
    }
*/
}
