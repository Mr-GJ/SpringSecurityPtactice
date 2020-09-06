package com.bootsecurity.bootsecurity.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

//    @PostMapping(value = "/login")
//    public String login(String uname, String pword) {
//        return "index";
//    }

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/syslog")
    public String showOrder() {
        return "syslog";
    }

    @GetMapping(value = "/sysuser")
    public String addUser() {
        return "sysuser";
    }

    @GetMapping(value = "/biz1")
    public String updateOrder() {
        return "biz1";
    }

    @GetMapping(value = "/biz2")
    public String deleteOrder() {
        return "biz2";
    }
}
