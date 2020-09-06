package com.bootsecurity.bootsecurity.config.jwt;

import com.bootsecurity.bootsecurity.config.exception.AjaxResponse;
import com.bootsecurity.bootsecurity.config.exception.CustomException;
import com.bootsecurity.bootsecurity.config.exception.CustomExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class JwtAuthController {

    @Resource
    JwtTokenService jwtTokenService;

    @RequestMapping(value = "/auth")
    public AjaxResponse auth(@RequestBody Map<String, String> token) {
        String username = token.get("username");
        String password = token.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                    "用户名或密码不正确"));
        }
        try {
            String authentoken = jwtTokenService.login(username,password);
            return AjaxResponse.success(authentoken);
        }catch (CustomException e){
            return AjaxResponse.error(e);
        }
    }

    @RequestMapping(value = "/refreshToken")
    public AjaxResponse refreshToken(@RequestHeader("${jwt.header}") String token) {
        String s = jwtTokenService.refreshToken(token);
        return AjaxResponse.success(s);
    }

}
