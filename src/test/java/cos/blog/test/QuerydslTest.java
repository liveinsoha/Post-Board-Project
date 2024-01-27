package cos.blog.test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.QBoard;
import cos.blog.web.model.entity.QMember;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Test
    void test() {

        Member member = new Member("aaa", "sss", "ddd");
        em.persist(member);
        Board savedBoard = new Board("qqq", "www", member);
        em.persist(savedBoard);
        em.flush();
        em.clear();

        JPAQueryFactory query = new JPAQueryFactory(em);

        QBoard board = QBoard.board;

        Board findBoard = query.select(board)
                .from(board)
                .where(board.id.eq(savedBoard.getId())).
                fetchOne();


        System.out.println(findBoard);
        assertThat(findBoard).isEqualTo(savedBoard);
    }
}
