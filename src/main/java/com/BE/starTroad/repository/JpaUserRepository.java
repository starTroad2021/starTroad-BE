package com.BE.starTroad.repository;

import com.BE.starTroad.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaUserRepository implements UserRepository{

    private final EntityManager em;

    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    public User save(User user) {

        em.persist(user);
        return user;
    }

    public Optional<User> findById(String email) {
        User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        return em.createQuery("select m from User m",User.class).getResultList();
    }

    /*public Optional<User> findByName(String name) {
        List<User> result = em.createQuery("select m from User m where m.name = :name", User.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }*/
}
