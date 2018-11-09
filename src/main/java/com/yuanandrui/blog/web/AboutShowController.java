package com.yuanandrui.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    private static final String ABOUT = "about";
    @GetMapping("/about")
    String about(){
        return ABOUT;
    }
}
