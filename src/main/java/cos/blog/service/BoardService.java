package cos.blog.service;

import cos.blog.model.entity.Board;
import cos.blog.model.entity.Member;
import cos.blog.repository.BoardRepository;
import cos.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.internal.MemberResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Board findById(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
    }

    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }
}
