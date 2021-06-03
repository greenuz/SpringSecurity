package com.greenux.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.greenux.security.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

// 이 패키지 만드는 이유는 시큐리티가 /login 주소 낚아 채서  로그인을 진행시키는데,
// 로그인을 진행이 완료되면 시큐리 session을 만들어준다.
// 같은 세션공간인데, 시큐리티 자신만의 세션공간을 가진다.  (Security ContextHolder)
// 이때 세션에 들어갈 수 있는 것은 오브젝트가 정해졌다. Authentication  타입 객체이다.
// Authentication  안에 user 정보가 있어야 된다.
// user오브젝트의 타입은 -> UserDetails 타입의 객체여야 한다.

// 쉽게 정리하면 시큐리티 세션 영역에 저장을 해주는데,
// 여기 들어갈 수 있는 객체가 -> Authentication 객체 -> Authentication 객체안에 User정보를 저장할 때 UserDetails 여야 한다.

//PrincipalDetails 는 UserDetails와 같은 타입이 된다. PrincipalDetails를 Authentication 객체 안에 넣을 수 있겠지.
@Data
public class PrincipalDetails implements UserDetails , OAuth2User{ // UserDetails 뿐만 아니라, OAuth2User 까지 implements 받아서, PrincipalDetails 에서 두개의 인터페이스에 대한 다중 상속을 받을 수 있다.

    private User user;//composition
    private Map<String,Object> attributes;

    //general login
    public PrincipalDetails(User user){
        this.user = user;
        
    }

    //oAuth Login
    public PrincipalDetails(User user,Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    //해당 User의 권한을 리턴하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority(){
            @Override
            public String getAuthority(){
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
    
        return user.getPassword();
    }

    @Override
    public String getUsername() {
     
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
       
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
    
}
