package cos.blog.service;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.BoardService;
import cos.blog.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private Member blogWriter;
    private Member replyWriter;
    private Board boardId;

    @BeforeEach
    void beforeEach() {
        blogWriter = new Member("Lee", "aaa", "qqq");
        memberService.join(blogWriter);
        System.out.println(blogWriter.getRole().toString());
        log.info("member = {}", blogWriter);

        replyWriter = new Member("ReplyWriter", "aaa", "sss");
        memberService.join(replyWriter);
    }

    @Test
    void addBoardTest() {
        Long boardId = boardService.addBoard("title", "content", blogWriter);
        Board findBoard = boardService.findById(boardId);
        log.info("findBoard = {}", findBoard);
        assertThat(findBoard.getMember()).isEqualTo(blogWriter);
    }

    @Test
    void addReply() {
        Long boardId = boardService.addBoard("title", "content", blogWriter);
        boardService.addReply(replyWriter.getId(), boardId, "replyreply");

        List<ReplyResponseDto> replyInBoard = boardService.findReplyInBoard(boardId);
        System.out.println(replyInBoard);
    }

}