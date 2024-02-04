package cos.blog.web.dto;

import cos.blog.web.model.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoardResponseDto {

    Long id;
    String title;
    String content;
    String writer;
    Long writerId;
    List<ReplyResponseDto> replys;
    LocalDateTime createdTime;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerId = board.getMember().getId();
        this.writer = board.getMember().getAccount();
        this.createdTime = board.getCreatedTime();
        this.replys = board.getReplies().stream()
                .map(ReplyResponseDto::new)
                .collect(Collectors.toList());
    }
}
