package cos.blog.web.repository.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.dto.BoardSearchDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.model.entity.QReply;
import cos.blog.web.util.QuerydslUtil;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Slf4j
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory query;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> findAllPaging(Pageable pageable, BoardSearchDto boardSearchDto) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<OrderSpecifier> ORDER = QuerydslUtil.getAllOrderSpecifiers(pageable);

        QBoard board = QBoard.board;

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
                .leftJoin(board.member, QMember.member)
                .fetchOne();
        log.info("쿼리 두 방");
        return new PageImpl<>(result, pageable, total);
    }


    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("author", searchBy)) {
            return QBoard.board.member.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return QBoard.board.content.like("%" + searchQuery + "%");
        }
        return null;
    }


}
