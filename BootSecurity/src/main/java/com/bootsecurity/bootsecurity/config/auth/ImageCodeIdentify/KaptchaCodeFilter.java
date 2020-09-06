package com.bootsecurity.bootsecurity.config.auth.ImageCodeIdentify;

import com.bootsecurity.bootsecurity.config.MyAuthenticationFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Component
public class KaptchaCodeFilter extends OncePerRequestFilter {

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/login", request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private boolean validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        String captchaCode = ServletRequestUtils.getStringParameter(request.getRequest(),
                "captchaCode");
        if (StringUtils.isEmpty(captchaCode)) {
            throw new SessionAuthenticationException("验证码不正确");
        }
        KaptchaImageVO kaptchaVO = (KaptchaImageVO) session.getAttribute("kaptcha_key");

        if (Objects.isNull(kaptchaVO)) {
            throw new SessionAuthenticationException("验证码不存在");
        }
        if (kaptchaVO.isExpired()) {
            session.removeAttribute("kaptcha_key");
            throw new SessionAuthenticationException("验证码过期");
        }
        if (!StringUtils.equals(captchaCode, kaptchaVO.getCode())) {
            throw new SessionAuthenticationException("验证码不匹配");
        }
        return true;
    }
}
