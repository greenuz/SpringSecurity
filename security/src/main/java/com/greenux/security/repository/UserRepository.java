package com.greenux.security.repository;

import com.greenux.security.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

//jparepository 기본적으로 crud 함수를 들고 있다.
//@Repository 라는 어노테이션이 없어도 IOC된다. 이유는 JpaRepository 를 상속했기 때문에.    
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByUsername(String username); //스프링에서 가지고 있는 문법 select * from user where username=?(parameter),더이상 작성 안해도됨. JPA Query Method
}
