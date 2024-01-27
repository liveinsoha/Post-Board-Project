package cos.blog.web.dto;

import cos.blog.web.model.entity.Board;
import lombok.Data;

@Data
public class BoardDto {

    String title;
    String content;
    String writer;

    public BoardDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember().getName();
    }
}
