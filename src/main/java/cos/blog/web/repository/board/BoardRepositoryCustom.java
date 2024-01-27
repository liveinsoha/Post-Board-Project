package cos.blog.web.repository.board;

import cos.blog.web.model.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findAllPaging(Pageable pageable);
}
