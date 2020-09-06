package com.bootsecurity.bootsecurity.config.auth.smscode;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private UserDetailsService userDetailsService;

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authentication1 = (SmsAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("无法根据手机号获取用户信息");
        }
        //添加用户的角色和权限：userDetails.getAuthorities()
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(userDetails,userDetails.getAuthorities());
        smsAuthenticationToken.setDetails(authentication1.getDetails());
        return smsAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authorities) {
        return SmsAuthenticationToken.class.isAssignableFrom(authorities);
    }
}
