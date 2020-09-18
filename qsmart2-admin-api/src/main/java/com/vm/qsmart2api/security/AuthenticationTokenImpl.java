/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.security;


import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2api.dtos.jwt.JwtUserDto;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class AuthenticationTokenImpl extends AbstractAuthenticationToken {
    
    private String username;

    public AuthenticationTokenImpl(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = principal;
    }

    public void authenticate() {
        if (getDetails() != null && getDetails() instanceof JwtUserDto ) {
            setAuthenticated(true);
           // System.out.println("===========>TRUE<=================");
        } else {
            //System.out.println("===========>FALSE<=================");
            setAuthenticated(false);
        }
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username != null ? username : "";
    }

//    public String getHash() {
//        return DigestUtils.md5DigestAsHex(String.format("%s_%d", username, ((Users) getDetails()).getCreated().getTime()).getBytes());
//    }

}
