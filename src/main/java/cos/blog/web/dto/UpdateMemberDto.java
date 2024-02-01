package cos.blog.web.dto;

import cos.blog.web.model.entity.Member;
import lombok.Data;

@Data
public class UpdateMemberDto {

    private String account;
    private String currentPw;
    private String name;
    private String password;
    private String email;


}
