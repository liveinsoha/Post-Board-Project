package cos.blog.web.dto;

import cos.blog.web.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberDto {

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.password = member.getPassword();
        this.email = member.getEmail();
    }

    private Long id;
    private String name;
    private String password;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .name(name)
                .password(password)
                .email(email)
                .build();
    }


}
