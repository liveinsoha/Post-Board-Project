package cos.blog.web.repository.reReply;

import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.ReReply;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.member.MemberRepository;
import cos.blog.web.repository.reply.ReplyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReReplyRepositoryTest {

    @Autowired
    ReReplyRepository reReplyRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    /**
     * exist테스트
     */
    @Test
    void test() {
        Member member1 = memberRepository.findById(1L).get();
        Board board = boardRepository.findById(1L).get();
        Reply reply = Reply.createReply(member1, board, "content1");

        Member member2 = memberRepository.findById(2L).get();

        ReReply.createReReply(member2,reply,"rereply1");
        ReReply.createReReply(member2,reply,"rereply2");
        ReReply.createReReply(member2,reply,"rereply3");

        replyRepository.save(reply);
        em.flush();
        em.clear();
        boolean exists = reReplyRepository.existsByReplyId(reply.getId());
        System.out.println("exists = " + exists);
    }


}