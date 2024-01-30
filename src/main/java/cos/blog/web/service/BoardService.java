package cos.blog.web.service;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.MemberRepository;
import cos.blog.web.repository.reply.ReplyRepository;
import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public Long addBoard(String title, String content, Member member) {
        Board board = new Board(title, content, member);
        boardRepository.save(board);
        return board.getId();
    }


    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
    }

    public List<ReplyResponseDto> findReplyInBoard(Long boardId) {
        List<ReplyResponseDto> replys = replyRepository.findReplys(boardId);
        return replys;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public Page<Board> findAllPaging(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllPaging(pageable);
        return boards;
    }

    @Transactional
    public Long addReply(Long memberId, Long boardId, String content) {
        replyRepository.mSave(memberId, boardId, content);
        return null;
    }

    @Transactional
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

}
