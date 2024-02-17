package cos.blog.web.repository.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.dto.BoardSearchDto;
import cos.blog.web.model.entity.*;
import cos.blog.web.util.QuerydslUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory query;
    private final QBoard board = QBoard.board;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override //컬레션 조회
    public Optional<Board> findBoardWithReplies(Long boardId) {
        Board findBoard = query.select(board).distinct()
                .from(board)
                .join(board.member, QMember.member).fetchJoin()
                .leftJoin(board.replies, QReply.reply).fetchJoin() //댓글이 없을 수도 있으니까 leftJoin(데이터가 없는 경우)
                .where(board.id.eq(boardId))
                .fetchOne();
        return Optional.ofNullable(findBoard);
    }

    @Override
    public Optional<Board> findBoardWithRepliesAndReReplies(Long boardId) {
        Board findBoard = query.select(board).distinct()
                .from(board)
                .join(board.member, QMember.member).fetchJoin()
                .leftJoin(board.replies, QReply.reply).fetchJoin()
                .leftJoin(QReply.reply, QReReply.reReply.reply)//댓글이 없을 수도 있으니까 leftJoin(데이터가 없는 경우)
                .where(board.id.eq(boardId))
                .fetchOne();
        return Optional.empty();
    }

    @Override
    public Optional<Board> findBoardWithMember(Long boardId) {
        Board findBoard = query.select(board).distinct()
                .from(board)
                .join(board.member, QMember.member).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne();
        return Optional.ofNullable(findBoard);
    }

    @Override
    public Page<Board> findAllPaging(Pageable pageable, BoardSearchDto boardSearchDto) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<OrderSpecifier> ORDER = QuerydslUtil.getAllOrderSpecifiers(pageable);

        List<Board> result = query.select(board)
                .from(board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .leftJoin(board.member, QMember.member).fetchJoin()
                .orderBy(ORDER.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(pageSize).fetch();

        log.info("쿼리 한 방");
        Long total = query.select(board.count())
                .from(board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .leftJoin(board.member, QMember.member)
                .fetchOne();
        log.info("쿼리 두 방");
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Board> findByMember(Pageable pageable, Long memberId) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<OrderSpecifier> ORDER = QuerydslUtil.getAllOrderSpecifiers(pageable);

        List<Board> result = query.select(board)
                .from(board)
                .where(board.member.id.eq(memberId))
                .orderBy(ORDER.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(pageSize)
                .fetch();
        log.info("쿼리 한 방");

        Long total = query.select(board.count())
                .from(board)
                .where(board.member.id.eq(memberId))
                .fetchOne();
        log.info("쿼리 두 방");
        return new PageImpl<>(result, pageable, total); //컨텐츠, pageable, 개수
    }


    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("author", searchBy)) {
            return board.member.account.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return board.content.like("%" + searchQuery + "%");
        }
        return null;
    }


}
