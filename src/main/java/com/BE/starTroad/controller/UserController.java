package com.BE.starTroad.controller;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String register(UserForm form) {
        //구글 로그인하면 받은 정보로 여기로 와야함...
        User user = new User();
        user.setName(form.getName()); //get email, name

        userService.join(user);

        return ""; //등록 후 보낼 url
    }
}
