package cos.blog.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyResponseDto {

    Long id;
    Long replyAuthorId;
    String replyAuthor;
    String content;
    LocalDateTime createdTime;


    public ReplyResponseDto(Long id, Long replyAuthorId, String replyAuthor, String content, LocalDateTime createdTime) {
        this.id = id;
        this.replyAuthorId = replyAuthorId;
        this.replyAuthor = replyAuthor;
        this.content = content;
        this.createdTime = createdTime;

    }


}
