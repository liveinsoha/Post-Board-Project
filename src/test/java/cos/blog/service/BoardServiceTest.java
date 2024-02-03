package cos.blog.service;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.ReReply;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.reply.ReplyRepository;
import cos.blog.web.service.BoardService;
import cos.blog.web.service.MemberService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@Slf4j
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager em;


    private Member blogWriter;
    private Member replyWriter;
    private Board boardId;

    @BeforeEach
    void beforeEach() {
        blogWriter = new Member("Lee", "이이", "aaa", "qqq");
        memberService.join(blogWriter);
        System.out.println(blogWriter.getRole().toString());
        log.info("member = {}", blogWriter);

        replyWriter = new Member("ReplyWriter", "이이", "aaa", "sss");
        memberService.join(replyWriter);
    }

    @Test
    void addBoardTest() {
        Board board = boardService.addBoard("title", "content", blogWriter);
        Board findBoard = boardService.findById(board.getId());
        log.info("findBoard = {}", findBoard);
        assertThat(findBoard.getMember()).isEqualTo(blogWriter);
    }

    @Test
    void addReply() {
        Board board = boardService.addBoard("title", "content", blogWriter);
        boardService.addReply(replyWriter.getId(), board.getId(), "replyreply");

        List<ReplyResponseDto> replyInBoard = boardService.findReplyInBoard(board.getId());
        System.out.println(replyInBoard);
    }

    @Test
    void addReReply() {
        Member member = memberService.findById(1L);
        Board board = boardService.findById(1L);
        boardService.addReply(member.getId(), board.getId(), "asdasd");

    }

    /**
     *
     */

    @Test
    void test() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply reply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        ReReply reReply1 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent1");
        ReReply reReply2 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent2");
        System.out.println("reReply = " + reReply1);
        System.out.println("reReply = " + reReply2);
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteBoard(board.getId());
        System.out.println("-------------------");
    }


    @Test
    @DisplayName("댓글 삭제 테스트 : 대댓글 있을 경우")
    void test2() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply parentReply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        ReReply reReply1 = boardService.addReReply(member2.getId(), parentReply.getId(), "reReplyContent1");
        ReReply reReply2 = boardService.addReReply(member2.getId(), parentReply.getId(), "reReplyContent2");
        System.out.println("reReply = " + reReply1);
        System.out.println("reReply = " + reReply2);
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteReply(parentReply.getId());
        System.out.println("-------------------");

        em.flush();
        em.clear();

        //영속성 컨텍스트에 업데이트된거 날렸으므로 다시 select 쿼리 날린다.
        Reply referenceById = replyRepository.getReferenceById(parentReply.getId());
        assertThat(referenceById.isDeleted()).isTrue();
    }

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @DisplayName("댓글 삭제 테스트 : 대댓글 없을 경우")
    void test3() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply parentReply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteReply(parentReply.getId());
        System.out.println("-------------------");

        assertThat(replyRepository.existsById(parentReply.getId())).isFalse();
    }


}