package com.yz.learn.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class CustomPasswordToken implements AuthenticationToken {

    private String token;

    public CustomPasswordToken(String token) {
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
