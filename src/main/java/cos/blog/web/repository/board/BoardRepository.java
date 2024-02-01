package cos.blog.web.repository.board;

import cos.blog.web.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Query("select b from Board b join fetch b.member m where b.id = :boardId")
    Optional<Board> findByIdWithMember(Long boardId);
}
