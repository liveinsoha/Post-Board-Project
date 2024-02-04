package cos.blog.service;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.ReReply;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.reply.ReplyRepository;
import cos.blog.web.service.BoardService;
import cos.blog.web.service.MemberService;
import jakarta.persistence.*;
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

    @PersistenceUnit
    EntityManagerFactory emf;

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
        Board board = boardService.addBoard("title", "content", blogWriter.getId());
        Board findBoard = boardService.findById(board.getId());
        log.info("findBoard = {}", findBoard);
        assertThat(findBoard.getMember()).isEqualTo(blogWriter);
    }

    @Test
    void addReply() {
        em.flush();
        em.clear();
        System.out.println("===================");
        Board board = boardService.addBoard("title", "content", blogWriter.getId());
        System.out.println("===================");
        em.flush();
        em.clear();
        System.out.println("===================");
        boardService.addReply(replyWriter.getId(), board.getId(), "replyreply"); //reply 엔티티 생성하여 저장.
        System.out.println("===================");
        em.flush();
        em.clear();
        System.out.println("===================");
        //  System.out.println("board = " + board);
        //  List<Reply> replies = boardService.findReplyInBoard(board.getId());
        System.out.println("===================");
        Board board1 = boardService.findById(board.getId());
        System.out.println(board.getReplies().get(0).getContent());
    }

    @Test
    void addReReply() {
        Member member = memberService.findById(1L);
        Board board = boardService.findById(1L);
        boardService.addReply(member.getId(), board.getId(), "asdasd");

    }

    @Test
    void 컬렉션조회_최적화_쿼리한방() {
        Board board = boardService.findBoardWithReplies(1L);
        System.out.println("board = " + board);
    }

    @Test
    void 컬렉션조회_batch() {
        Board board = boardService.findBoardWithRepliesCanPaging(1L); //ToOne관계만 페치조인.
        System.out.println("board = " + board);
        //댓글들은 where reply.board.id = boardId
        //대댓글들은 in쿼리
        //대댓글의 작성자 member도 in쿼리 -> toOne관계도 페치조인하지않고 LazyLoading전략일 경우 BatchSize의 영향을 받아 in쿼리를 가져온다
        //네트워크를 더 많이 타므로 TOOne관계는 fetchJoin으로 가져오자

        /**
         * V3방법은 모드 페치 조인으로 다루면 JPA에서 distinct처리를 하기 전에 많은 양의 데이터 중봅된 것이 넘어온다 -> 데이터 전송량이 많다는 단점이 있다.
         * V3의 방법보다 Batch를 활용하면 쿼리는 3개 나가도 테이블 단위로 in으로 팍팍팍 찍어서 가져오기 때문에 데이터 전송량이 줄어든다/
         */
    }

    @Test
    @DisplayName("BoardId로 댓글 대댓글 조회 -> BatchSize활용")
    void findRepliesInBoard() {
        List<ReplyResponseDto> repliesInBoard = boardService.findRepliesInBoard(1L);
        System.out.println("repliesInBoard = " + repliesInBoard);
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