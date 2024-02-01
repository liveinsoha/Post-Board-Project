package cos.blog.web.repository.board;

import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void test() {
        Pageable pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Member member = memberRepository.findById(1L).get();
        Page<Board> boards = boardRepository.findByMember(pageRequest, member.getId());

        System.out.println("boards.getContent() = " + boards.getContent());
    }


}