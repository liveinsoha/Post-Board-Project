package cos.blog.web.repository.like;

import cos.blog.web.model.entity.Reply;
import cos.blog.web.model.entity.ReplyLikeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeLogRepository extends JpaRepository<ReplyLikeLog, Long>, ReplyLikeLogRepositoryCustom {

}
