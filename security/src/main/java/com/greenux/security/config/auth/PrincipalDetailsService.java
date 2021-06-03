package com.greenux.security.config.auth;

import com.greenux.security.model.User;
import com.greenux.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Authentication 객체를 만들어서 세션에 넣어보자.
//SecurityConfig 설정에서 loginProcessingUrl("/login"); 해놨는데, /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
//함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다. 서비스가 호출될 때 함수 종료시 AuthenticationPrincipal  어노테이션이 만들어진다는 것을 기억하자.
@Service //메모리에 띄어준다.
public class PrincipalDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity !=null){
            return new PrincipalDetails(userEntity);
        }

        return null;
    }
    
}
