package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {

    @RequestMapping("/index")
    public String toIndex(Model mav) {
        mav.addAttribute("msg", "这是一条message");
        return "index";
    }
}
