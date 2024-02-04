package cos.blog.web.repository.reply;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.model.entity.QReply;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.util.QuerydslUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory query;
    private final QReply reply = QReply.reply;

    public ReplyRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


/*    @Override
    public List<ReplyResponseDto> findReplyByBoard(Long boardId) {

        List<Tuple> tuples = query.select(reply.id, reply.board.title, reply.content, reply.member.account, reply.createdTime, reply.member.id)
                .from(reply)
                .join(reply.member, QMember.member)
                .join(reply.board, QBoard.board)
                .where(reply.board.id.eq(boardId))
                .fetch();

        log.info("tuples = {}", tuples);

        List<ReplyResponseDto> replys = new ArrayList<>();
        for (Tuple tuple : tuples) {
            Long id = tuple.get(reply.id);
            Long replyAuthorId = tuple.get(reply.member.id);
            String replyAuthor = tuple.get(reply.member.account);
            String boardTitle = tuple.get(reply.board.title);
            String content = tuple.get(reply.content);
            LocalDateTime createdTime = tuple.get(reply.createdTime);
            replys.add(new ReplyResponseDto(id, boardTitle, boardId, replyAuthorId, replyAuthor, content, createdTime));
        }

        return replys;
    }*/

    @Override
    public List<Reply> findReplyWithBoardAndMember(Long boardId) {

        List<Reply> replies = query.select(reply)
                .from(reply)
                .join(reply.member, QMember.member).fetchJoin() //ToOne관계는 페치조인
                .join(reply.board, QBoard.board).fetchJoin() //ToOne관계는 페치조인
                .where(reply.board.id.eq(boardId))
                .fetch();

        return replies;
    }

    @Override
    public Page<ReplyResponseDto> findReplyByMember(Pageable pageable, Long memberId) {

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<OrderSpecifier> ORDER = QuerydslUtil.getAllOrderSpecifiers(pageable);

        List<Reply> replies = query.select(reply)
                .from(reply)
                .join(reply.member, QMember.member).fetchJoin()
                .join(reply.board, QBoard.board).fetchJoin()
                .where(reply.member.id.eq(memberId)) //작성자 id
                .orderBy(ORDER.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(pageSize)
                .fetch();

        log.info("replies = {}", replies);

        Long totalCount = query.select(reply.count())
                .from(reply)
                .where(reply.member.id.eq(memberId))
                .fetchOne();


        List<ReplyResponseDto> replyResponseDtos = replies.stream().map(ReplyResponseDto::new).collect(Collectors.toList());

        return new PageImpl<>(replyResponseDtos, pageable, totalCount);
    }

}
