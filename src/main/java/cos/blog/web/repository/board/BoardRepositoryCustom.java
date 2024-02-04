package cos.blog.web.repository.board;

import cos.blog.web.dto.BoardSearchDto;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findBoardWithReplies(Long boardId);
    Optional<Board> findBoardWithRepliesAndReReplies(Long boardId);

    Page<Board> findAllPaging(Pageable pageable, BoardSearchDto boardSearchDto);

    Optional<Board> findBoardWithMember(Long boardId);

    Page<Board> findByMember(Pageable pageable, Long memberId);


}
