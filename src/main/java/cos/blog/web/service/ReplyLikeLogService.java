package cos.blog.web.service;

import cos.blog.web.model.entity.Member;
import cos.blog.web.model.entity.Reply;
import cos.blog.web.model.entity.ReplyLikeLog;
import cos.blog.web.repository.member.MemberRepository;
import cos.blog.web.repository.like.ReplyLikeLogRepository;
import cos.blog.web.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyLikeLogService {

    private final ReplyLikeLogRepository replyLikeLogRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void like(Long memberId, Long replyId) {
        Optional<ReplyLikeLog> result = replyLikeLogRepository.findByMemberIdAndReplyId(memberId, replyId);
        Reply reply = replyRepository.findById(replyId).orElseThrow(NoSuchElementException::new);

        /*-- 좋아요 토글 --*/
        if (result.isEmpty()) {
            Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
            ReplyLikeLog replyLikeLog = ReplyLikeLog.createReplyLikeLog(member, reply);
            reply.increaseLikeCount();
            replyLikeLogRepository.save(replyLikeLog);
        } else {
            reply.decreaseLikeCount();
            replyLikeLogRepository.delete(result.get());
        }
    }
}
