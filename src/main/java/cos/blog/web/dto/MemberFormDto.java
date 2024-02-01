package cos.blog.web.dto;

import cos.blog.web.model.entity.Member;
import lombok.Data;

@Data
public class MemberFormDto {

    private String account;
    private String name;
    private String password;
    private String email;

    public MemberFormDto(Member member) {
        this.account = member.getAccount();
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
