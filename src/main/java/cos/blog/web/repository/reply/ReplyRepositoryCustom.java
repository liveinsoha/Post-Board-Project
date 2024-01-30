package cos.blog.web.repository.reply;

import cos.blog.web.dto.ReplyResponseDto;

import java.util.List;

public interface ReplyRepositoryCustom {

    List<ReplyResponseDto> findReplys(Long BoardId);
}
