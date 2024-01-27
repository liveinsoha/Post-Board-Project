package cos.blog.web.controller;

import cos.blog.web.dto.JoinFormDto;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private BCryptPasswordEncoder passwordEncoder;

    private final MemberService memberService;

    @GetMapping("/member/join")
    public String join(Model model) {
        model.addAttribute("joinFormDto", new JoinFormDto());
        return "/member/joinForm";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute JoinFormDto joinFormDto) {
        log.info("joinFormDto = {}", joinFormDto);

        Member member = Member.of(joinFormDto.getName(), passwordEncoder.encode(joinFormDto.getPassword()), joinFormDto.getEmail());
        log.info("member = {}", member);
        memberService.join(member);

        return "redirect:/";
    }
}
