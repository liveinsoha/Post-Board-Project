package cos.blog.web.repository.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import cos.blog.web.util.QuerydslUtil;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory query;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> findAllPaging(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<OrderSpecifier> ORDER = QuerydslUtil.getAllOrderSpecifiers(pageable);

        QBoard board = QBoard.board;

        List<Board> result = query.select(board)
                .from(board)
                .leftJoin(board.member, QMember.member)
               // .orderBy(ORDER.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(pageSize).fetch();

        Long total = query.select(board.count())
                .from(board)
                .leftJoin(board.member, QMember.member)
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }
}
