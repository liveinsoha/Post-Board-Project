package cos.blog.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import cos.blog.web.model.entity.ReReply;
import cos.blog.web.model.entity.Reply;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ReplyResponseDto {

    private String deletedMessage = "삭제된 메시지 입니다";

    Long id;
    Long boardId;
    String boardTitle;
    Long replyAuthorId;
    String account;
    String maskedName;
    String content;
    List<ReReplyResponseDto> reReplies;
    String createdTime;
    int reReplyCount;
    int likeCount;
    boolean isDeleted;


    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.boardTitle = reply.getBoard().getTitle();
        this.boardId = reply.getBoard().getId();
        this.account = reply.getMember().getAccount();
        this.replyAuthorId = reply.getMember().getId();
        this.maskedName = createMaskedId(reply.getMember().getAccount());
        this.content = reply.getContent();
        this.createdTime = createWroteDate(reply.getCreatedTime());
        this.reReplyCount = reply.getReReplyCount();
        this.likeCount = reply.getLikeCount();
        this.isDeleted = reply.isDeleted();
        log.info("대댓글 in쿼리 로드 =======================시작");
        reReplies = reply.getReReplies().stream().map(ReReplyResponseDto::new).collect(Collectors.toList());
        log.info("대댓글 in쿼리 로드 =======================끝");
    }

    private String createWroteDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        int hour = currentDate.toLocalTime().getHour();
        int minute = currentDate.getMinute();
        return year + "." + month + "." + day + ". " + hour + ":" + minute;
    }


    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
