package cos.blog.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReplyResponseDto {

    String replyAuthor;
    String content;


    @QueryProjection
    public ReplyResponseDto(String replyAuthor,String content) {
        this.replyAuthor = replyAuthor;
        this.content = content;

    }


}
