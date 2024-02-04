package cos.blog.web.controller.ReReply;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.BaseResponse;
import cos.blog.web.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reReply")
@RequiredArgsConstructor
public class ReReplyController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Object> addReReply(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             Long replyId, String content) {
        Long memberId = principalDetails.getMember().getId();
        boardService.addReReply(memberId,replyId,content);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "대댓글 등록 완료", true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * //이 경로로 접근하려면 인증객체 필요하다. -> delete로 url접근 직접 못한다.
     * 인증객체 id와 ReReply id가같을 때 삭제버튼이 나타나고 그것을 통해서만 삭제할 수 있다.
     * @return
     */
    @DeleteMapping("/{reReplyId}")
    public ResponseEntity<Object> deleteReply(@PathVariable Long reReplyId){
        System.out.println(reReplyId);
        boardService.deleteReReply(reReplyId);

        BaseResponse response = new BaseResponse(HttpStatus.ACCEPTED, "댓글이 삭제 되었습니다", true);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
