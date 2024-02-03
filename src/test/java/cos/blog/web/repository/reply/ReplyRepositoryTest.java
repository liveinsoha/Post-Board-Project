package cos.blog.web.repository.reply;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Member;
import cos.blog.web.repository.member.MemberRepository;
import cos.blog.web.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardService boardService;

    @BeforeEach
    void beforeEch() {
        Member member = memberRepository.findById(1L).get();
        for (int i = 1; i <= 10; i++) {
            boardService.addReply(member.getId(), 30L, "reply" + i);
        }
    }

    @Test
    void test() {

        Member member = memberRepository.findById(1L).get();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<ReplyResponseDto> replyByMember = replyRepository.findReplyByMember(pageRequest, member.getId());

        System.out.println("replyByMember.getContent = " + replyByMember.getContent());

    }
}