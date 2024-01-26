package cos.blog.test;

import cos.blog.api.dto.UpdateMemberRequest;
import cos.blog.api.dto.UpdateMemberResponse;

import cos.blog.dto.MemberDto;
import cos.blog.model.entity.Member;
import cos.blog.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CRUDControllerTest {

    @Autowired
    MemberService memberService;


    @PostMapping("/join")
    public String join(@RequestBody MemberDto member) {
        Member memberEntity = member.toEntity();
        memberService.join(memberEntity);
        log.info("join member = {}", memberEntity);
        return "회원 가입 완료";
    }


    @PostMapping("/update/{memberId}")
    public UpdateMemberResponse update(@PathVariable Long memberId, @RequestBody UpdateMemberRequest updatememberRequest) {
        Member updated = memberService.update(memberId, updatememberRequest.getName(), updatememberRequest.getPassword(), updatememberRequest.getEmail());
        return new UpdateMemberResponse(updated);
    }

    @GetMapping("/paging/member")
    public Page<MemberDto> page(@RequestParam int pageNo, @RequestParam String criteria){
        Page<Member> membersPaging = memberService.findMembersPaging(pageNo, criteria);
        Page<MemberDto> memberDTOs = membersPaging.map(MemberDto::new);
        return memberDTOs;
    }
}
