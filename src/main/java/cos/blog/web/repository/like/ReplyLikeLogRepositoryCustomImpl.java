package cos.blog.web.repository.like;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.model.entity.QReplyLikeLog;
import cos.blog.web.model.entity.ReplyLikeLog;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ReplyLikeLogRepositoryCustomImpl implements ReplyLikeLogRepositoryCustom {

    private final JPAQueryFactory query;
    private final QReplyLikeLog replyLikeLog = QReplyLikeLog.replyLikeLog;

    public ReplyLikeLogRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ReplyLikeLog> findByMemberIdAndReplyId(Long memberId, Long replyId) {

        ReplyLikeLog replyLikeLog1 = query.select(replyLikeLog)
                .from(replyLikeLog)
                .where(replyLikeLog.member.id.eq(memberId),
                        replyLikeLog.reply.id.eq(replyId))
                .fetchOne();

        return Optional.ofNullable(replyLikeLog1);

    }
}
