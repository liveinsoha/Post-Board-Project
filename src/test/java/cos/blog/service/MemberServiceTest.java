package cos.blog.service;

import cos.blog.web.model.entity.Member;
import cos.blog.web.service.MemberService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
//@Rollback(value = false)
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    void test(){
        Member member = Member.builder().name("Lee").password("aaa").email("qqq").build();
        em.persist(member);
        em.flush();
        em.clear();
        memberService.findById(member.getId());
    }

    @Test
    void test1() {
        Member member = Member.builder().name("Lee").password("aaa").email("qqq").build();
        Long savedId = memberService.join(member);
        em.flush();
        em.clear();
        Member findmember = memberService.findById(savedId);
        assertThat(findmember.getId()).isEqualTo(member.getId());
    }

    @Test
    void test2() {
        Member member = Member.builder().name("Lee").password("aaa").email("qqq").build();
        Long savedId = memberService.join(member);
        Member update = memberService.update(savedId, "Kim", "aaa", "qqq");
        Member findmember = memberService.findById(savedId);
        assertThat(findmember.getName()).isEqualTo("Kim");
    }
}