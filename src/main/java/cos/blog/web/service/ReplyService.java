package cos.blog.web.service;

import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.MemberRepository;
import cos.blog.web.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Long addReply(Long memberId, Long boardId, String content) {
        Member member = memberRepository.findById(memberId).get();
        Board board = boardRepository.findById(boardId).get();
        Reply reply = new Reply(content, board, member);
        replyRepository.save(reply);
        return reply.getId();
    }

    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

}
