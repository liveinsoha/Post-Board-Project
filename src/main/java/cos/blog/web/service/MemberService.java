package cos.blog.web.service;


import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.exception.NoSuchMemberException;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.repository.MemberRepository;
import cos.blog.web.repository.board.BoardRepository;
import cos.blog.web.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public Page<Board> findBoardByMember(Long memberId, Pageable pageable) {
        Page<Board> boards = boardRepository.findByMember(pageable, memberId);
        return boards;
    }

    public Page<ReplyResponseDto> findReplyByMember(Long memberId,Pageable pageable) {
        Page<ReplyResponseDto> replyByMember = replyRepository.findReplyByMember(pageable, memberId);
        return replyByMember;
    }


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

    public Page<Member> findAll(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members;
    }

    public Optional<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }
}
