package cos.blog.web.service;

import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.reply.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 각각의 테스트를 독립적으로 수행.
class ReplyLikeLogServiceTest {

    @Autowired
    ReplyLikeLogService replyLikeLogService;
    @Autowired
    ReplyRepository replyRepository;

    @Test
    void test1() {
        replyLikeLogService.like(1L, 1L);
        Reply reply = replyRepository.findById(1L).get();

        assertThat(reply.getLikeCount()).isEqualTo(1);
    }

    @Test
    void test2() {
        replyLikeLogService.like(1L, 1L);
        replyLikeLogService.like(1L, 1L);
        Reply reply = replyRepository.findById(1L).get();

        assertThat(reply.getLikeCount()).isEqualTo(0);
    }

}