package com.BE.starTroad;

import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

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
