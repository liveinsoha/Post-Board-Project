package cos.blog.web.repository.reply;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.model.entity.QReply;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory query;

    public ReplyRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<ReplyResponseDto> findReplys(Long boardId) {

        QReply reply = QReply.reply;

        List<Tuple> tuples = query.select(reply.id, reply.content, reply.member.name, reply.createdTime, reply.member.id)
                .from(reply)
                .join(reply.member, QMember.member)
                .where(reply.board.id.eq(boardId))
                .fetch();

        log.info("tuples = {}", tuples);

        List<ReplyResponseDto> replys = new ArrayList<>();
        for (Tuple tuple : tuples) {
            Long id = tuple.get(reply.id);
            Long replyAuthorId = tuple.get(reply.member.id);
            String replyAuthor = tuple.get(reply.member.name);
            String content = tuple.get(reply.content);
            LocalDateTime createdTime = tuple.get(reply.createdTime);
            replys.add(new ReplyResponseDto(id, replyAuthorId, replyAuthor, content, createdTime));
        }

        return replys;
    }
}
