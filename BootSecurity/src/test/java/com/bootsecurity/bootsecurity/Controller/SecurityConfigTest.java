package com.bootsecurity.bootsecurity.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith("SpringRunner.class")
@SpringBootTest
class SecurityConfigTest {
    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    public void pwdEncoder(){
        //$2a$10$Bst3L6Nb05awCG/lnUJUFeKsmmOH77rlNdUjxhQKSd.8cfz/cmsB6
        System.out.println(passwordEncoder.encode("123456"));
    }


}