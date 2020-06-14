package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {

    @RequestMapping("/first")
    public String index() {
        System.out.println("进入" + this.getClass().getName() + "执行index()方法处理映射：/first................");
        return "destination";
    }
}
