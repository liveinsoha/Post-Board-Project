package cos.blog.web.controller.member;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.board.dto.PagingBoardDto;
import cos.blog.web.controller.board.dto.PagingReplyDto;
import cos.blog.web.dto.*;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/member/myPage")
    public String myPage(Model model,
                         @Qualifier("board") @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable boardPageable,
                         @Qualifier("reply") @PageableDefault(sort = "createdTime", direction = Sort.Direction.DESC) Pageable replyPageable,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        Page<BoardResponseDto> boardResponses = memberService.findBoardByMember(memberId, boardPageable).map(BoardResponseDto::new);
        Page<ReplyResponseDto> replyResponses = memberService.findReplyByMember(memberId, replyPageable);
        PagingBoardDto pagingBoardDto = new PagingBoardDto(boardResponses);
        PagingReplyDto pagingReplyDto = new PagingReplyDto(replyResponses);

        System.out.println("pagingBoardDto = " + pagingBoardDto);
        System.out.println("pagingReplyDto = " + pagingReplyDto);

        model.addAttribute("boards", boardResponses);
        model.addAttribute("replys",replyResponses);
        model.addAttribute("pagingBoard", pagingBoardDto);
        model.addAttribute("pagingReply", pagingReplyDto);
        return "/member/myPage";
    }

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
