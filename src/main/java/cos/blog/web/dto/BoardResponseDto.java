package cos.blog.web.dto;

import cos.blog.web.model.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    Long id;
    String title;
    String content;
    String writer;
    List<ReplyResponseDto> replys;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember().getName();
    }
}
