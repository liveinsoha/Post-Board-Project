package cos.blog.web.dto;

import cos.blog.web.model.entity.Member;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
