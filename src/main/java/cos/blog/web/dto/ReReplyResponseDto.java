package cos.blog.web.dto;

import cos.blog.web.model.entity.ReReply;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReReplyResponseDto {

    Long reReplyId;
    Long replyId;
    Long reReplyAuthorId;
    String account;
    String content;
    String maskedName;
    String createdTime;

    public ReReplyResponseDto(ReReply reReply) {
        this.reReplyId = reReply.getId();
        this.replyId = reReply.getReply().getId();
        this.reReplyAuthorId = reReply.getMember().getId();
        this.account = reReply.getMember().getAccount();
        this.content = reReply.getContent();
        this.maskedName = createMaskedId(reReply.getMember().getAccount());
        this.createdTime = createWroteDate(reReply.getCreatedTime());
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
