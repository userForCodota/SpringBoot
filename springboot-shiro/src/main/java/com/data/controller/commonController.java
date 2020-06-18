package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class commonController {


    @RequestMapping("index")
    public String toIndex() {
        return "index";
    }
}
