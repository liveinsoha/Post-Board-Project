package cos.blog.controller;

import cos.blog.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {



    @GetMapping("/")
    public String main1() {
        return "home";
    }




}
