package com.yuanandrui.blog.web.admin;

import com.yuanandrui.blog.po.User;
import com.yuanandrui.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    private static final String LOGIN = "admin/login";
    private static final String INDEX = "admin/index";
    private static final String REDIRECT_ADMIN = "redirect:/admin";
    private static final String REDIRECT_LOGOUT = "redirect:/";


    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage(){
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user", user);
            return INDEX;
        }else{
            attributes.addFlashAttribute("message", "Username or password is not existing");
            return REDIRECT_ADMIN;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return REDIRECT_LOGOUT;
    }
}
