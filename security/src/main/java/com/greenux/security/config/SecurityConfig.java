package com.greenux.security.config;

import com.greenux.security.config.oauth.PrincipalOauth2UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //메모리에 떠야해서 해당 어노테이션 붙여준다.
@EnableWebSecurity // 활성화를 시켜놔야 한다. 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true , prePostEnabled = true) //secure annotation 의 활성화, preAuthorize 의 활성화 
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean //해당 메서드의 리턴되는 오브젝트를 ioc로 등록해준다.
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/user/**").authenticated() // /user 로그인 한 사람만 들어올 수 있고, 인증만 되면 들어갈 수 있는 주소
            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //manager 로그인 했지만, 매니저나 어드민 권한이 있어야 들어갈 수 있고
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //admin 어드민 권한이 있어야 들어갈 수 있
            .anyRequest().permitAll() //위의 3개 주소가 아닌경우에는 권한이 더 허용이 된다.
            .and() //권한이 필요한 로그인 페이지로 매핑을 시켜줄 때 적어준다.아래로 2줄
            .formLogin()
            .loginPage("/loginForm")
            .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행준다. 따라서 컨트롤러에 /login 을 안만들어도 된다.
            .defaultSuccessUrl("/")
            .and() //google Login
            .oauth2Login() 
            .loginPage("/loginForm") //google Login 완료된 뒤 후 처리가 필요함
            .userInfoEndpoint()
            .userService(principalOauth2UserService);

    }
    
}
