package cos.blog.web.controller.board;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.BaseResponse;
import cos.blog.web.dto.*;
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
        model.addAttribute("boardForm", new BoardForm());
        return "/board/boardForm";
    }



    @PostMapping("/board/createBoard")
    public String postBoard(@ModelAttribute BoardForm boardFormDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boardService.addBoard(boardFormDto.getTitle(), boardFormDto.getContent(), principalDetails.getMember());
        return "redirect:/";
    }

    @GetMapping("/board/details/{boardId}")
    public String details(@PathVariable Long boardId, Model model) {
        Board board = boardService.findByIdWithMember(boardId);
        List<ReplyResponseDto> replyInBoard = boardService.findReplyInBoard(boardId);
        BoardResponseDto boardFormDto = new BoardResponseDto(board);
        boardFormDto.setReplys(replyInBoard);
        log.info("boardFormDto = {}", boardFormDto);
        model.addAttribute("board", boardFormDto);
        return "/board/details";
    }

    @GetMapping("/board/edit/{boardId}")
    public String editForm(Model model, @PathVariable Long boardId) {
        log.info("게시판 수정 Get");
        Board board = boardService.findById(boardId);
        System.out.println("board = " + board);
        model.addAttribute("editForm", new BoardForm(board));
        return "/board/editForm";
    }

    @PostMapping("/board/edit/{boardId}")
    public String editBoard(@PathVariable Long boardId, BoardForm editForm) {
        log.info("게시판 수정 Post");
        boardService.editBoard(boardId, editForm.getTitle(), editForm.getContent());
        return "redirect:/board/details/" + boardId;
    }

    @PostMapping("/board/reply/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> addReply(ReplyRequestDto replyRequestDto) {
        boardService.addReply(replyRequestDto.getMemberId(), replyRequestDto.getBoardId(), replyRequestDto.getContent());
        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "댓글이 등록되었습니다.", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/board/reply/{boardId}/{replyId}")
    @ResponseBody
    public ResponseEntity<Object> deleteReply(@PathVariable Long replyId) {
        log.info("replyId = {}", replyId);
        boardService.deleteReply(replyId);
        BaseResponse response = new BaseResponse(HttpStatus.ACCEPTED, "댓글이 삭제되었습니다", true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
