package com.bootsecurity.bootsecurity.config.auth.ImageCodeIdentify;

import java.time.LocalDateTime;

public class KaptchaImageVO {

    private String code;

    private LocalDateTime expireTime;
    public KaptchaImageVO(String code, int expireAfterTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterTime);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

}
