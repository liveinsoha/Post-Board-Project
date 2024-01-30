package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Reply {

    public Reply(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public Reply(String content, Member member, Board board) {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private Long id;

    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @CreatedDate
    LocalDateTime createdDate;
}
