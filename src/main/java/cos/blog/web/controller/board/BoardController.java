package cos.blog.web.controller.board;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.BaseResponse;
import cos.blog.web.dto.BoardResponseDto;
import cos.blog.web.dto.ReplyRequestDto;
import cos.blog.web.dto.ReplyResponseDto;
import cos.blog.web.dto.ResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.service.BoardService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/createBoard")
    public String createBoard(Model model) {

        model.addAttribute("boardForm", new BoardResponseDto());
        return "/board/boardForm";
    }

    @PostMapping("/board/createBoard")
    public String postBoard(@ModelAttribute BoardResponseDto boardFormDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boardService.addBoard(boardFormDto.getTitle(), boardFormDto.getContent(), principalDetails.getMember());
        return "redirect:/";
    }

    @GetMapping("/board/details/{boardId}")
    public String details(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId);
        List<ReplyResponseDto> replyInBoard = boardService.findReplyInBoard(boardId);
        BoardResponseDto boardFormDto = new BoardResponseDto(board);
        boardFormDto.setReplys(replyInBoard);
        log.info("boardFormDto = {}", boardFormDto);
        model.addAttribute("board", boardFormDto);
        return "/board/details";
    }

    @GetMapping("/board/reply/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> addReply(ReplyRequestDto replyRequestDto) {
        boardService.addReply(replyRequestDto.getMemberId(), replyRequestDto.getBoardId(), replyRequestDto.getContent());
        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "댓글이 등록되었습니다.", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/board/reply/{boardId}/{replyId}")
    @ResponseBody
    public ResponseEntity<Object> deleteReply(@PathVariable Long replyId) {
        em.flush();
        em.clear();
        log.info("replyId = {}", replyId);
        boardService.deleteReply(replyId);
        BaseResponse response = new BaseResponse(HttpStatus.ACCEPTED, "댓글이 삭제되었습니다", true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
