package cos.blog.web.controller.member;

import cos.blog.web.dto.CheckIdDto;
import cos.blog.web.dto.JoinFormDto;
import cos.blog.web.dto.MemberTestDto;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private BCryptPasswordEncoder passwordEncoder;

    private final MemberService memberService;

    @GetMapping("/member/join")
    public String join(Model model) {
        model.addAttribute("joinFormDto", new JoinFormDto());
        return "/member/joinForm1";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute JoinFormDto joinFormDto) {
        log.info("joinFormDto = {}", joinFormDto);

        Member member = Member.of(joinFormDto.getName(), passwordEncoder.encode(joinFormDto.getPassword()), joinFormDto.getEmail());
        log.info("member = {}", member);
        memberService.join(member);

        return "redirect:/";
    }

    @PostMapping("/member/join1")
    @ResponseBody
    public String join1(@RequestBody MemberTestDto memberTestDto) {
        System.out.println("memberTestDto = " + memberTestDto);
        return "회원가입";
    }

    @PostMapping("/member/checkId")
    @ResponseBody
    public String checkId(@RequestBody String checkId) {
        System.out.println("checkId = " + checkId);
        Optional<Member> result = memberService.findByName(checkId);
        if (result.isEmpty()) {
            return "Available";
        } else {
            return "no";
        }
    }


}
