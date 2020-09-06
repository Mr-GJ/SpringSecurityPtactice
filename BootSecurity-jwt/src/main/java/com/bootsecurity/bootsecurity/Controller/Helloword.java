package com.bootsecurity.bootsecurity.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class Helloword {
    @RequestMapping(value = "/helloword")
    public String helloword(String words, HttpServletResponse response){
//        response.addHeader("Access-Control-Allow-Origin", "http://localhost:8888");
        return "HelloWord Miss Ge "+words;
    }
}
