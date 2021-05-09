package com.BE.starTroad.controller;

import com.BE.starTroad.config.JwtTokenUtil;
import com.BE.starTroad.domain.JwtRequest;
import com.BE.starTroad.domain.JwtResponse;
import com.BE.starTroad.domain.User;
import com.BE.starTroad.helper.constants.SocialLoginType;
import com.BE.starTroad.repository.JpaUserRepository;
import com.BE.starTroad.service.*;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/auth")
@Slf4j
public class OauthController {

    @Autowired
    private OauthService oauthService;
    //@Autowired
    //private UserService userService;
    @Autowired
    private JpaUserService jpaUserService;

    @Autowired
    private JwtTokenService jwtTokenService;


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

    @GetMapping(value = "/{socialLoginType}/token")
    public ResponseEntity<> getToken(@PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                     @RequestParam(name = "code") String code,
                                     @RequestParam(name = "redirect_uri") String redirect_uri) {
        System.out.println(">> FE에게 받은 authorization code : " + code);
        System.out.println(">> FE에게 받은 redirect_uri : " + redirect_uri);
        //String result = oauthService.requestAccessToken_Info(socialLoginType, code);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value ="/{socialLoginType}/callback")
    public ResponseEntity<String> callback(
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
            String name = jsonObj.get("user_name").toString();
            int length = email.length();
            email = email.substring(1,length-1);
            length = name.length();
            name = name.substring(1,length-1);
            Optional<User> dbUser = jpaUserService.findByEmail(email);

            if (dbUser.isPresent()) {
                System.out.println("Already registered USER");
            }
            else {
                System.out.println("new USER");
                User newUser = new User();
                //이메일이랑 이름 뒤에 "" 떼기
                newUser.setEmail(email);
                newUser.setName(name);
                jpaUserService.save(newUser);
                }

            final String token = jwtTokenService.createJwtToken(email,name);
            if(token == null) { //token 생성이 안된 경우
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            else {
                return new ResponseEntity<String>(token,HttpStatus.OK);
                //return ResponseEntity.ok(new JwtResponse(token));
            }
        }
        else {
            //accessToken 얻지 못함
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
