package com.bootsecurity.bootsecurity.config.auth;

import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component("rabcService")
public class MyRBACService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Resource
    private MyRBACServiceMapper myRBACServiceMapper;

    public boolean haspermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails)principal).getUsername();
            List<String> urls = myRBACServiceMapper.findUrlByUserName(username);
            return urls.stream().anyMatch(url -> antPathMatcher.match(url, request.getRequestURI()));

        }
        return false;
    }
}
