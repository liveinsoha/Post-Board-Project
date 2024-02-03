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

    @ManyToOne()
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @ManyToOne
    @JoinColumn(name = "REPLY_ID")
    Reply reply;

    public static ReReply createReReply(Member member, Reply reply, String content) {
        ReReply reReply = new ReReply();
        reReply.member = member;
        reReply.reply = reply;
        reply.addReReply(reReply);
        reReply.content = content;
        return reReply;
    }

}
