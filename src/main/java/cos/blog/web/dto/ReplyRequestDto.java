package cos.blog.web.dto;

import lombok.Data;

@Data
public class ReplyRequestDto {

    Long memberId;
    Long boardId;
    String content;
}
