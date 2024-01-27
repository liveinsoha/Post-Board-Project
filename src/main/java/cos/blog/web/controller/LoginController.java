package cos.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/login/loginForm";
    }

    @GetMapping("/login")
    public String login() {
        return "/login/loginForm";
    }
}
