package cos.blog.web.repository.reReply;

import cos.blog.web.model.entity.ReReply;
import cos.blog.web.repository.reply.ReplyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReReplyRepository extends JpaRepository<ReReply, Long> ,ReReplyRepositoryCustom {
    @Modifying
    @Query(value = "INSERT INTO RE_REPLY(MEMBER_ID, REPLY_ID, CONTENT) VALUES(:memberId, :replyId, :content)", nativeQuery = true)
    int mSave(Long memberId, Long replyId, String content);




}
