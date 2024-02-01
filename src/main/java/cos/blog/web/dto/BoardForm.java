package cos.blog.web.dto;

import cos.blog.web.model.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardForm {

    Long id;
    String title;
    String content;


    public BoardForm(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}
