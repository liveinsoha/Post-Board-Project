package cos.blog.web.repository.board;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<Board> findAllPaging(Pageable pageable);

}
