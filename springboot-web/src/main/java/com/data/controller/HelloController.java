package com.data.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //@RequestMapping(value = "hello")
    @RequestMapping("/hello")
    public String helloController(String name) {
        return "你好！" + (name == null ? "" : name);
    }
}
