package cos.blog.web.api.dto;

import lombok.Data;

@Data
public class UpdateMemberRequest {

    private String name;
    private String password;
    private String email;
}
