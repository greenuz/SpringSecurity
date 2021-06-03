package com.greenux.security.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes; //oauth2User.getAttributes();
    public NaverUserInfo(Map<String, Object>attributes){
        System.out.println(attributes);
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {
        // TODO Auto-generated method stub
        return "naver";
    }

    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return (String) attributes.get("name");
    }
}
