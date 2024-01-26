package cos.blog.controller;

import cos.blog.dto.JoinFormDto;
import cos.blog.dto.MemberDto;
import cos.blog.model.entity.Member;
import cos.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/join")
    public String join(Model model) {
        model.addAttribute("joinFormDto", new JoinFormDto());
        return "/member/joinForm";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute JoinFormDto joinFormDto) {
        log.info("joinFormDto = {}", joinFormDto);

        Member member = Member.from(joinFormDto);
        log.info("member = {}", member);
        memberService.join(member);

        return "redirect:/";
    }
}
