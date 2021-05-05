package com.BE.starTroad.controller;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.helper.constants.SocialLoginType;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.service.JpaUserService;
import com.BE.starTroad.service.OauthService;
import com.BE.starTroad.service.UserService;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/auth")
@Slf4j
public class OauthController {

    @Autowired
    private OauthService oauthService;
    //@Autowired
    //private UserService userService;
    @Autowired
    private JpaUserService jpaUserService;

    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        System.out.println(">> 사용자로부터의 소셜 로그인 요청 : "+socialLoginType);
        try {
            oauthService.request(socialLoginType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @GetMapping(value ="/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name ="code") String code) throws ParseException {
        System.out.println(">> 소셜 로그인 서버로부터 받은 code : " + code);

        String result = oauthService.requestAccessToken_Info(socialLoginType, code);
        System.out.println(result);
        if (result != null){

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(result);
            JSONObject jsonObj = (JSONObject) obj;

            String email = jsonObj.get("user_email").toString();

            /*if (userService.findOne(email) != null) {
                System.out.println("Already registered USER");
                //홈화면
                return "/starTroad";
            }
            else {
                System.out.println("new USER");
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(jsonObj.get("user_name").toString());
                userService.join(newUser);
                //홈화면
                return "/starTroad";
            }*/
            if (jpaUserService.findByEmail(email) != null) {
                System.out.println("Already registered USER");
                //홈화면
                return "/starTroad";
            }
            else {
                System.out.println("new USER");
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(jsonObj.get("user_name").toString());
                //userService.join(newUser);
                //홈화면
                return "/starTroad";
            }
        }
        else {
            //accessToken 얻지 못함
            return "구글 로그인 토큰 요청 실패";
        }
    }
}