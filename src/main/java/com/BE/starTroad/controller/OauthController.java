package com.BE.starTroad.controller;

import com.BE.starTroad.domain.User;
import com.BE.starTroad.helper.constants.SocialLoginType;
import com.BE.starTroad.repository.JpaUserRepository;
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
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/auth")
@Slf4j
public class OauthController {

    @Autowired
    private OauthService oauthService;
    private UserService userService;

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

        if (result != null){
            //result 통해서 db에 등록되어 있는 유저인지 check

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(result);
            JSONObject jsonObj = (JSONObject) obj;

            String email = jsonObj.get("email").toString();

            if (userService.findOne(email).isPresent()) {
                System.out.println("hiiiiii");
            }
            else {

            }

            return result;
        }
        else {
            //accessToken 얻지 못함
            return "구글 로그인 토큰 요청 실패";
        }

    }
}