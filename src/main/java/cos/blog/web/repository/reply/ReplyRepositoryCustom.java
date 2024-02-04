package cos.blog.web.repository.reply;

import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.model.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyRepositoryCustom {

    List<Reply> findReplyWithBoardAndMember(Long BoardId);

    Page<ReplyResponseDto> findReplyByMember(Pageable pageable, Long memberId);


}
