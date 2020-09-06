package com.bootsecurity.bootsecurity.Controller;

import com.bootsecurity.bootsecurity.config.*;
import com.bootsecurity.bootsecurity.config.auth.smscode.SmsCodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Random;

@Slf4j
@RestController
public class SmsController  {
    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @GetMapping(value = "/smscode")
    public AjaxResponse sms(@RequestParam String mobile , HttpSession session){
        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(mobile);
        if(myUserDetails == null){
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "手机号未注册"));
        }
        SmsCodeEntity smsCodeEntity = new SmsCodeEntity(RandomStringUtils.randomNumeric(4), 60, mobile);
        log.info(smsCodeEntity.getCode()+"->手机号:"+mobile);
        session.setAttribute("smscode_key",smsCodeEntity);
        return AjaxResponse.success("短信验证码已发送");
    }
}
