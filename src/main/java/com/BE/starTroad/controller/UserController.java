package com.BE.starTroad.controller;

import com.BE.starTroad.domain.Roadmap;
import com.BE.starTroad.domain.User;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.service.JpaUserService;
import com.BE.starTroad.service.UserService;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    JpaUserService jpaUserService;

    @PostMapping(value="/login/add")
    public ResponseEntity<User> addInfo(User user){

        String email = user.getEmail();
        User updateUser = jpaUserService.update(email, user);

        if (updateUser != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/{user_email}")
    public ResponseEntity<User> getInfo(@PathVariable("user_email") String email) {
        Optional<User> findUser = jpaUserService.findByEmail(email);

        if (findUser.isPresent()) {
            return new ResponseEntity<User>(findUser.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(value="/{user_email}")
    public ResponseEntity<User> saveInfo(@PathVariable("user_email") String email, User user) {
        User updateUser = jpaUserService.update(email, user);

        if (updateUser != null) {
            return new ResponseEntity<User>(updateUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
