package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Slf4j
@Getter
@ToString(exclude = {"member", "board"}) //얘네를 ToString에 찍기 위한 쿼리가 또 나간다.
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

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ReReply> reReplies = new ArrayList<>();

    @Column(name = "LIKE_COUNT", nullable = false)
    private int likeCount;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted; //삭제 여부 판단

    @Column(name = "Re_REPLY_COUNT", nullable = false)
    private int reReplyCount;

    public void addReReply(ReReply reReply) {
        reReplies.add(reReply);
    }

    public static Reply createReply(Member member, Board board, String content) {
        Reply reply = new Reply();
        reply.member = member;
        reply.board = board;
        reply.content = content;
        reply.likeCount = 0;
        reply.reReplyCount = 0;
        reply.isDeleted = false;
        log.info("board select 발생 넣기 --");
        board.addReply(reply);
        log.info("board select 발생 넣기 --");
        return reply;
    }

    public void increaseReReplyCount() {
        this.reReplyCount++;
    }

    public void decreaseReReplyCount() {
        this.reReplyCount--;
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

//    @Override
//    public String toString() {
//        System.out.println("=================================");
//        return "Reply{" +
//                "id=" + id +
//                ", content='" + content + '\'' +
//                ", board=" + board +
//                ", member.getId()=" + member.getId() +
//                ", reReplies=" + reReplies.stream().map(ReReply::getId).collect(Collectors.toList()) +
//                ", likeCount=" + likeCount +
//                ", isDeleted=" + isDeleted +
//                '}';
//    }
}
