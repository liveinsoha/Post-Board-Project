package cos.blog.api.dto;

import cos.blog.model.entity.Member;
import lombok.Data;

@Data
public class UpdateMemberResponse {

    Long id;
    private String name;
    private String password;
    private String email;

    public UpdateMemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.password = member.getPassword();
        this.email = member.getEmail();
    }
}
