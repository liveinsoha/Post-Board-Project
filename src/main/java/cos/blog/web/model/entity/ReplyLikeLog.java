package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //지연로딩 프록시 기술을 위함
public class ReplyLikeLog {

    private ReplyLikeLog(Member member, Reply reply) {
        this.member = member;
        this.reply = reply;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @JoinColumn(name = "REPLY_ID")
    @ManyToOne(fetch = FetchType.LAZY) //다대일, 연관관계 주인
    Reply reply;

    public static ReplyLikeLog createReplyLikeLog(Member member, Reply reply) {
        return new ReplyLikeLog(member, reply);
    }

}
