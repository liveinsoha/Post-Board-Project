package cos.blog.web.dto;

import cos.blog.web.model.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    Long id;
    String title;
    String content;
    String writer;
    Long writerId;
    List<ReplyResponseDto> replys;
    LocalDateTime createdDate;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerId = board.getMember().getId();
        this.writer = board.getMember().getAccount();
        this.createdDate = board.getCreatedDate();
    }
}
