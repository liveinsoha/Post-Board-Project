package cos.blog.web.controller.reply;

import cos.blog.web.controller.BaseResponse;
import cos.blog.web.dto.ReplyRequestDto;
import cos.blog.web.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
public class replyController {

    private final BoardService boardService;

    @PostMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> addReply(ReplyRequestDto replyRequestDto) {
        boardService.addReply(replyRequestDto.getMemberId(), replyRequestDto.getBoardId(), replyRequestDto.getContent());
        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "댓글이 등록되었습니다.", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{boardId}/{replyId}")
    @ResponseBody
    public ResponseEntity<Object> deleteReply(@PathVariable Long replyId) {
        log.info("replyId = {}", replyId);
        boardService.deleteReply(replyId);
        BaseResponse response = new BaseResponse(HttpStatus.ACCEPTED, "댓글이 삭제되었습니다", true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
