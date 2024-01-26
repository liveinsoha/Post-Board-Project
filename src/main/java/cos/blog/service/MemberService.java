package cos.blog.service;


import cos.blog.exception.NoSuchMemberException;
import cos.blog.model.entity.Member;
import cos.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Member update(Long id, String name, String password, String email) {
        Optional<Member> findmember = memberRepository.findById(id);
        Member member = findmember.orElseThrow(NoSuchMemberException::new);
        member.updateMember(name, password, email);
        return member;
    }

    public Member findById(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.orElseThrow(NoSuchMemberException::new);
    }

    public Page<Member> findMembersPaging(int pageNo, String criteria) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, criteria));
        Page<Member> all = memberRepository.findAll(pageRequest);
        return all;
    }
}
