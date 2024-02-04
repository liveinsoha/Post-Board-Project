package cos.blog.web.controller.board;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.controller.BaseResponse;
import cos.blog.web.dto.*;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Reply;
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

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        boardService.addBoard(boardFormDto.getTitle(), boardFormDto.getContent(), principalDetails.getMember().getId());
        return "redirect:/";
    }

    @GetMapping("/board/details/{boardId}")
    public String details(@PathVariable Long boardId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
       log.info("GET : board/details/{}", boardId);

        Board board = boardService.findBoardWithMember(boardId); //ToOne관계는 Board -> Member 페치조인으로 조인
        BoardResponseDto boardFormDto = new BoardResponseDto(board);
        List<ReplyResponseDto> repliesInBoardDto = boardService.findRepliesInBoard(boardId);
        log.info("boardFormDto = {}", boardFormDto);
        model.addAttribute("board", boardFormDto);
        model.addAttribute("replies", repliesInBoardDto);
        String account = findAccountForCheckingIdentification(principalDetails);
        model.addAttribute("account", account);
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






    private String findAccountForCheckingIdentification(PrincipalDetails principalDetails) {
        String account = "";
        if (principalDetails != null) {
            account = principalDetails.getMember().getAccount();
        } else {
            account = "";
        }
        return account;
    }
}
