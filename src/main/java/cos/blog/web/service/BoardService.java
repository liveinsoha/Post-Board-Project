package cos.blog.web.service;

import cos.blog.web.dto.BoardSearchDto;
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

    public Board findByIdWithMember(Long id){
        Board board = boardRepository.findByIdWithMember(id).orElseThrow(NoSuchElementException::new);
        return board;
    }

    @Transactional
    public void editBoard(Long boardId, String title, String content) {
        Board board = boardRepository.findById(boardId).get();
        board.editBoard(title, content);
    }


    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
    }

    public List<ReplyResponseDto> findReplyInBoard(Long boardId) {
        List<ReplyResponseDto> replys = replyRepository.findReplyByBoard(boardId);
        return replys;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public Page<Board> findAllPaging(Pageable pageable, BoardSearchDto boardSearchDto) {
        Page<Board> boards = boardRepository.findAllPaging(pageable, boardSearchDto);
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
