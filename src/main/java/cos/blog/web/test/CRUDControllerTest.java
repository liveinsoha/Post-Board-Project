package cos.blog.web.test;

import cos.blog.web.api.dto.UpdateMemberRequest;
import cos.blog.web.api.dto.UpdateMemberResponse;

import cos.blog.web.dto.MemberDto;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    public Page<MemberDto> page(
            @PageableDefault(size = 5, page = 0, sort = "createdDate", direction = Sort.Direction.DESC)Pageable pageable){
        Page<Member> membersPaging = memberService.findAll(pageable);
        Page<MemberDto> memberDTOs = membersPaging.map(MemberDto::new);
        return memberDTOs;
    }
}
