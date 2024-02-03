package cos.blog.web.repository.like;


import cos.blog.web.model.entity.ReplyLikeLog;

import java.util.Optional;

public interface ReplyLikeLogRepositoryCustom {
    Optional<ReplyLikeLog> findByMemberIdAndReplyId(Long MemberId, Long ReplyId);

}
