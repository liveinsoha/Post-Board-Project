package cos.blog.web.service;

import cos.blog.web.dto.BoardSearchDto;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.ReReply;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.member.MemberRepository;
import cos.blog.web.repository.reReply.ReReplyRepository;
import cos.blog.web.repository.reply.ReplyRepository;
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
    private final ReReplyRepository reReplyRepository;

    @Transactional
    public Board addBoard(String title, String content, Member member) {
        Board board = Board.createBoard(member, title, content);
        return boardRepository.save(board);
    }

    public Board findByIdWithMember(Long id) {
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
    public void deleteBoard(Long boardId) { //댓글 삭제 쿼리 개선..?
        boardRepository.deleteById(boardId);
    }

    public Page<Board> findAllPaging(Pageable pageable, BoardSearchDto boardSearchDto) {
        Page<Board> boards = boardRepository.findAllPaging(pageable, boardSearchDto);
        return boards;
    }

    @Transactional
    public Reply addReply(Long memberId, Long boardId, String content) {
        Member member = memberRepository.getReferenceById(memberId); //엔티티 생성에 불필요한 select문 없다.
        Board board = boardRepository.getReferenceById(boardId);
        Reply reply = Reply.createReply(member, board, content);
        return replyRepository.save(reply);
    }

    @Transactional
    public ReReply addReReply(Long memberId, Long replyId, String content) {
        Member member = memberRepository.getReferenceById(memberId);
        Reply reply = replyRepository.getReferenceById(replyId);
        ReReply reReply = ReReply.createReReply(member, reply, content);
        return reReplyRepository.save(reReply);
    }

    @Transactional
    public void deleteReReply(Long reReplyId) {
        ReReply reReply = reReplyRepository.getReferenceById(reReplyId);
        Reply parentReply = reReply.getReply();
        if (!parentReply.isDeleted()) { /*-- 댓글이 삭제되지 않았다면 --*/
            reReplyRepository.deleteById(reReplyId); // 대댓글만 삭제
            return;
        }
        //부모 댓글이 isDeleted라면
        reReplyRepository.deleteById(reReplyId); //대댓글 삭제 후

        if (!reReplyRepository.existsByReplyId(parentReply.getId())) {// 자식 대댓글 더 존재하는 지 확인
            replyRepository.deleteById(parentReply.getId()); // 없으면 부모댓글 엔티티 삭제.
        }
    }


    /**
     * 대댓글이 없을 경우 해당 댓글을 DB에서 그냥 삭제
     * 대댓글이 있을 경우 댓글을 삭제하지 않고 '블락'처리하고 대댓글을 볼 수 있도록 한다. isDeleted -> 블락
     * 대댓글이 모드 제거된 경우, 댓글이 isDelete(블락)상태라면 DB에서 댓글을 삭제한다.
     *
     * @param replyId
     */
    @Transactional
    public void deleteReply(Long replyId) {
        if (reReplyRepository.existsByReplyId(replyId)) { //자식 대댓글이 있다면
            Reply reply = replyRepository.getReferenceById(replyId);
            reply.setDeleted();
            return;
        }

        replyRepository.deleteById(replyId); //대댓글 없다면 댓글 엔티티 삭제.
        //  replyRepository.deleteById(replyId); //
    }

}
