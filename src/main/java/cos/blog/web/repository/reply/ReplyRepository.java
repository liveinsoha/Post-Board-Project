package cos.blog.web.repository.reply;

import cos.blog.web.model.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {


    @Modifying
    @Query(value = "INSERT INTO reply(member_Id, board_Id, content, CREATED_DATE) VALUES(:memberId, :boardId, :content, now())", nativeQuery = true)
    int mSave(Long memberId, Long boardId, String content); // 업데이트된 행의 개수를 리턴해줌.


}
