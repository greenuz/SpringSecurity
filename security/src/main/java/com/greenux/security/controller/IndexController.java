package com.greenux.security.controller;

import com.greenux.security.config.auth.PrincipalDetails;
import com.greenux.security.model.User;
import com.greenux.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //return view
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal PrincipalDetails principaldetails){ //DI(Dependancy Injection)
        System.out.println("/test/login ====================");
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();//return type object
        System.out.println("authentication :"+ principalDetails.getUser());


        System.out.println("userDetails:" + userDetails.getUsername());

        System.out.println("userDetails: " + principaldetails.getUser());

        return "session info check";

    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authenticatio, @AuthenticationPrincipal OAuth2User oauth){ //DI(Dependancy Injection)
        System.out.println("/test/login ====================");
        OAuth2User oAuth2User = (OAuth2User)authenticatio.getPrincipal();//return type object
        System.out.println("authentication :"+ oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());
        return "session info check";

    }

    @GetMapping({"","/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails: " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //SpringSecurity 파일 생성 후 스프링시큐리티가 해당 주소를 낚아 채지 않는다. ㄷ.ㄷ
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); //회원가입은 잘됨, 하지만 비밀번호가 1234 일경우 -> 시큐리티로 로그인할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문.

        return "redirect:/loginForm";
    }

    //@Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "personal info";
    }

}
