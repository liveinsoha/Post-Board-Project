package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    private static String deletedMessage = "삭제된 댓글입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReReply> reReplies = new ArrayList<>();

    @Column(name = "LIKE_COUNT", nullable = false)
    private int likeCount;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted; //삭제 여부 판단

    public void addReReply(ReReply reReply) {
        reReplies.add(reReply);
    }

    public static Reply createReply(Member member, Board board, String content) {
        Reply reply = new Reply();
        reply.member = member;
        reply.board = board;
        board.addReply(reply);
        reply.content = content;
        reply.likeCount = 0;
        reply.isDeleted = false;
        return reply;
    }


    public void setDeleted() {
        this.isDeleted = true;
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }
}
