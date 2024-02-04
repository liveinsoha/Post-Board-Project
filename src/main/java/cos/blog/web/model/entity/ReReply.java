package cos.blog.web.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"reply"})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReReply extends BaseEntity { //대댓글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPLY_ID")
    Reply reply;


    public static ReReply createReReply(Member member, Reply reply, String content) {
        ReReply reReply = new ReReply();
        reReply.member = member;
        reReply.reply = reply;
        reReply.content = content;
        reply.addReReply(reReply);
        reply.increaseReReplyCount();
        return reReply;
    }

}
