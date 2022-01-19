package com.tionkior.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author : TionKior
 * @date : 2022/1/19 16:36
 */

public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
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
