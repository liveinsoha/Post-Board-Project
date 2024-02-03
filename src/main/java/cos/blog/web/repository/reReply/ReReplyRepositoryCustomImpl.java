package cos.blog.web.repository.reReply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.model.entity.QReReply;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReReplyRepositoryCustomImpl implements ReReplyRepositoryCustom {

    JPAQueryFactory query;
    QReReply reReply = QReReply.reReply;

    public ReReplyRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public boolean existsByReplyId(Long replyId) {
        Integer fetchFirst = query.selectOne()
                .from(reReply)
                .where(reReply.reply.id.eq(replyId))
                .fetchFirst();//댓글Id에 해당하는 값이 없으면 null 반환.
        return fetchFirst != null;

    }
}
