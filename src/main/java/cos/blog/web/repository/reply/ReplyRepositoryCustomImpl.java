package cos.blog.web.repository.reply;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.dto.QReplyResponseDto;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.model.entity.QReply;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

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

        List<Tuple> tuples = query.select(reply.content, reply.member.name)
                .from(reply)
                .join(reply.member, QMember.member)
                .where(reply.board.id.eq(boardId))
                .fetch();

        log.info("tuples = {}", tuples);

        List<ReplyResponseDto> replys = new ArrayList<>();
        for (Tuple tuple : tuples) {
            String replyAuthor = tuple.get(reply.member.name);
            String content = tuple.get(reply.content);
            replys.add(new ReplyResponseDto(replyAuthor, content));
        }

        return replys;
    }
}
