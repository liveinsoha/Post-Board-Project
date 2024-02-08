package cos.blog.web.controller.member;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.board.dto.PagingBoardDto;
import cos.blog.web.controller.board.dto.PagingReplyDto;
import cos.blog.web.dto.*;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    private final MemberService memberService;



    @GetMapping("/member/myPage")
    public String myPage(Model model,
                         @Qualifier("board") @PageableDefault(size = 5, sort = "board_createdTime", direction = Sort.Direction.DESC) Pageable boardPageable,
                         @Qualifier("reply") @PageableDefault(sort = "reply_createdTime", direction = Sort.Direction.DESC) Pageable replyPageable,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        Page<BoardResponseDto> boardResponses = memberService.findBoardByMember(memberId, boardPageable).map(BoardResponseDto::new);
        Page<ReplyResponseDto> replyResponses = memberService.findReplyByMember(memberId, replyPageable);
        PagingBoardDto pagingBoardDto = new PagingBoardDto(boardResponses);
        PagingReplyDto pagingReplyDto = new PagingReplyDto(replyResponses);

        System.out.println("pagingBoardDto = " + pagingBoardDto);
        System.out.println("pagingReplyDto = " + pagingReplyDto);

        model.addAttribute("boards", boardResponses);
        model.addAttribute("replys", replyResponses);
        model.addAttribute("pagingBoard", pagingBoardDto);
        model.addAttribute("pagingReply", pagingReplyDto);
        return "member/myPage";
    }

    @GetMapping("/member/join")
    public String join() {
        //    model.addAttribute("joinFormDto", new JoinFormDto());
        return "member/joinForm";
    }

    @PostMapping("/member/join")
    public String join(@RequestBody MemberFormDto joinFormDto) {
        log.info("joinFormDto = {}", joinFormDto);

        Member member = Member.of(joinFormDto.getAccount(), joinFormDto.getName(), joinFormDto.getPassword(), joinFormDto.getEmail());
        log.info("member = {}", member);
        memberService.join(member);

        return "redirect:/login";
    }


    @GetMapping("/member/details/{memberId}")
    public String details(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        MemberFormDto memberFormDto = new MemberFormDto(member);
        model.addAttribute("member", memberFormDto);
        return "member/details";
    }

    @GetMapping("/member/edit/{memberId}")
    public String edit(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        MemberFormDto memberFormDto = new MemberFormDto(member);
        model.addAttribute("member", memberFormDto);
        return "member/editForm";
    }


    /**
     * 현재 비밀번호 확인하고, 옳지 않을 경우 false, 옳을 경우 변경을 완료하고 details로 리다이렉트
     */
    @PostMapping("/member/edit/{memberId}")
    @ResponseBody
    public boolean edit(@PathVariable Long memberId,
                        @RequestBody UpdateMemberDto updateForm,
                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (memberService.checkPw(memberId, updateForm.getCurrentPw())) {
            Member member = memberService.editMember(memberId, updateForm);
            principalDetails.setMember(member); //세션 수정
            return true;
        }
        return false;
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
        Optional<Member> result = memberService.findByAccount(checkId);
        if (result.isEmpty()) {
            return "Available";
        } else {
            return "no";
        }
    }


}
