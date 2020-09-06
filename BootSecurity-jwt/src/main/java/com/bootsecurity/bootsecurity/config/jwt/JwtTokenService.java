package com.bootsecurity.bootsecurity.config.jwt;

import com.bootsecurity.bootsecurity.config.MyUserDetailsService;
import com.bootsecurity.bootsecurity.config.exception.CustomException;
import com.bootsecurity.bootsecurity.config.exception.CustomExceptionType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.undo.CannotUndoException;

@Service
public class JwtTokenService {
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    MyUserDetailsService myUserDetailsService;
    @Resource
    JwtTokenUtil jwtTokenUtil;

    public String login(String username,String password) throws CustomException{
        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }catch (AuthenticationException e){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名或密码不正确");
        }
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        return jwtTokenUtil.generateToken(userDetails);
    }

    public String refreshToken(String oldToken){
        if(!jwtTokenUtil.isTokenExpired(oldToken)){
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }
}
