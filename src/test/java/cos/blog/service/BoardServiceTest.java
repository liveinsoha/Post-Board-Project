package cos.blog.service;

import cos.blog.model.entity.Board;
import cos.blog.model.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Slf4j
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberService memberService;

    private Long savedId;

    @BeforeEach
    void beforeEach() {
        Member member = new Member("Lee", "aaa", "qqq");
        savedId = memberService.join(member);
        log.info("member = {}", member);
    }

    @Test
    void addBoardTest() {
        Long boardId = boardService.addBoard("title", "content", savedId);
        Board findBoard = boardService.findById(boardId);
        log.info("findBoard = {}", findBoard);
        assertThat(findBoard.getTitle()).isEqualTo("title");
    }

}