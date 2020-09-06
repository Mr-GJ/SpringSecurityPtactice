package com.bootsecurity.bootsecurity.config.auth.smscode;

import java.time.LocalDateTime;

public class SmsCodeEntity {

    private String code;
    private String mobile;
    private LocalDateTime expireTime;

    public SmsCodeEntity(String code, int expireAfterTime,String mobile) {
        this.mobile = mobile;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public String getMobile() {
        return mobile;
    }
}
