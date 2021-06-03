package com.greenux.security.config.oauth;

import javax.swing.plaf.synth.SynthScrollBarUI;

import com.greenux.security.config.auth.PrincipalDetails;
import com.greenux.security.config.oauth.provider.FacebookUserInfo;
import com.greenux.security.config.oauth.provider.GoogleUserInfo;
import com.greenux.security.config.oauth.provider.OAuth2UserInfo;
import com.greenux.security.model.User;
import com.greenux.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다. 서비스가 호출될 때 함수 종료시 AuthenticationPrincipal  어노테이션이 만들어진다는 것을 기억하자.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: "+ userRequest.getClientRegistration()); //registrationId 로어떤 oauth로 로그인했는지 확인가능.
        System.out.println("getAccessToken: "+ userRequest.getAccessToken().getTokenValue()); //token 은 현 상황에서는 우선 큰 중요하진 않음.
        //구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴 -> AccessToken 요청
        //userRequest  정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필 받안다.
        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("OAuth getAttributes: " + oauth2User.getAttributes());


        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("google login Request");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());            
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("facebook login Request");
            oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes()); 
        }else{
            System.out.println("we support only google & facebook");
        }


        String provider = oAuth2UserInfo.getProvider(); //google
        String providerid = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerid; // google_1981023981029380~~~~~~
        String provideremail = oauth2User.getAttribute("email");
        String password = bCryptPasswordEncoder.encode("greenux");
        String role = "ROLE_USER";

        User userEntity= userRepository.findByUsername(username);
        if(userEntity== null){
            userEntity = User.builder()
                .username(username)
                .password(password)
                .email(provideremail)
                .role(role)
                .provider(provider)
                .providerid(providerid)
                .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("you're already authenticated and authorizationed");
        }
        


        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
