package com.bootsecurity.bootsecurity.config.auth.smscode;

import com.bootsecurity.bootsecurity.config.MyAuthenticationFailureHandler;
import com.bootsecurity.bootsecurity.config.MyUserDetails;
import com.bootsecurity.bootsecurity.config.MyUserDetailsServiceMapper;
import com.bootsecurity.bootsecurity.config.auth.ImageCodeIdentify.KaptchaImageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Component
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/smslogin", request.getRequestURI())
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

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        SmsCodeEntity smsCodeEntity = (SmsCodeEntity) session.getAttribute("smscode_key");
        String mobileNumber = ServletRequestUtils.getStringParameter(request.getRequest(),
                "mobile");
        String smsCodeNumber = request.getParameter("smsCode");
        if (StringUtils.isEmpty(mobileNumber)) {
            throw new SessionAuthenticationException("手机号为空");
        }
        if (StringUtils.isEmpty(smsCodeNumber)) {
            throw new SessionAuthenticationException("短信验证码为空");
        }

        if (Objects.isNull(smsCodeEntity)) {
            throw new SessionAuthenticationException("短信验证码不存在");
        }
        if (smsCodeEntity.isExpired()) {
            session.removeAttribute("smscode_key");
            throw new SessionAuthenticationException("短信验证码过期");
        }
        if (!StringUtils.equals(smsCodeNumber, smsCodeEntity.getCode())) {
            throw new SessionAuthenticationException("短信验证码不匹配");
        }
        if (!StringUtils.equals(mobileNumber, smsCodeEntity.getMobile())) {
            throw new SessionAuthenticationException("与目标手机号码不匹配");
        }
        MyUserDetails userDetails = myUserDetailsServiceMapper.findByUserName(mobileNumber);
        if(Objects.isNull(userDetails)){
            throw new SessionAuthenticationException("输入的手机号未注册");
        }
        session.removeAttribute("smscode_key");
    }
}
