package cos.blog.web.service;

import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long addBoard(String title, String content, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Board board = new Board(title, content, member);
        boardRepository.save(board);
        return board.getId();
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public Page<Board> findAllPaging(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllPaging(pageable);
        return boards;
    }
}