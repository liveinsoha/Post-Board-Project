package cos.blog.test;


import cos.blog.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class httpControllerTest {

    @GetMapping("/get")
    public MemberDto getmember() {
        MemberDto member = MemberDto.builder()
                .name("Lee")
                .id(1L)
                .password("qqq")
                .build();

        return member;
    }

    @PostMapping("/post")
    public String post(@RequestBody MemberDto member) {
        System.out.println(member);

        return "post Ok";
    }
}
