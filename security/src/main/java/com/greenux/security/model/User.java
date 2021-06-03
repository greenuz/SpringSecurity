package com.greenux.security.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String provider; //google
    private String providerid; //google_sub(attribute information)
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerid, Timestamp createDate){
        this.username = username;
        this.password= password;
        this.email = email;
        this.role= role;
        this.provider= provider;
        this.providerid = providerid;
        this.createDate= createDate;
    }
}
