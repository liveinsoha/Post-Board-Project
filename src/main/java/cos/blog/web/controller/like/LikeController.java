package cos.blog.web.controller.like;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.BaseResponse;
import cos.blog.web.service.ReplyLikeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/like")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final ReplyLikeLogService replyLikeLogService;

    @PostMapping
    public ResponseEntity<Object> like(@AuthenticationPrincipal PrincipalDetails principalDetails, Long replyId ) {

        Long memberId = principalDetails.getMember().getId();
        replyLikeLogService.like(memberId,replyId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "좋아요 처리되었습니다", true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
