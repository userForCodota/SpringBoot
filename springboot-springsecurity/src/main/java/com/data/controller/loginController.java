package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {
    @RequestMapping("/toMyLoginPage")
    public String toMyLoginPage() {
        return "myLoginPage";
    }


//    @RequestMapping("/login")
//    public String doLogin() {
////        System.out.println(user);
////        System.out.println(password);
//        return "index";
//    }
}
