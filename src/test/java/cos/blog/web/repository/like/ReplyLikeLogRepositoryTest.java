package cos.blog.web.repository.like;

import cos.blog.web.model.entity.ReplyLikeLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyLikeLogRepositoryTest {

    @Autowired
    ReplyLikeLogRepository replyLikeLogRepository;

    @Test
    void test() {
        replyLikeLogRepository.findByMemberIdAndReplyId(1L, 2L);
        System.out.println("---");

    }
}