package com.data.controller;

import com.data.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class indexController {

    @RequestMapping("/index")
    public String toIndex(Model mav) {
        mav.addAttribute("text", "th:text标签将内容识别成text");
        mav.addAttribute("utext", "<h1 style='color:red;'>th:utext将内容识别成html</h1>");
        Map<String, Object> someObjects = getSomeObjects();
        mav.addAttribute("list", someObjects.get("list"));
        return "index";
    }

    private Map<String, Object> getSomeObjects() {
        HashMap<String, Object> resultMap = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new User().setName("张" + i).setGender(i).setAge(i));
        }
        resultMap.put("list", list);
        return resultMap;
    }

}
