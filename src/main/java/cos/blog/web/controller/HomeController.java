package cos.blog.web.controller;

import cos.blog.web.controller.board.dto.PagingBoardDto;
import cos.blog.web.dto.BoardResponseDto;
import cos.blog.web.dto.BoardSearchDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private BoardService boardService;

    @GetMapping("")
    public String home(Model model,
                       @PageableDefault(size = 5, sort = "board_createdTime", direction = Sort.Direction.DESC)
                       Pageable pageable, BoardSearchDto boardSearchDto) {

        log.info("pageable = {}", pageable);
        log.info("boardSearchDto = {}", boardSearchDto);

        Page<Board> boards = boardService.findAllPaging(pageable, boardSearchDto);
        Page<BoardResponseDto> responses = boards.map(BoardResponseDto::new);
        PagingBoardDto paging = new PagingBoardDto(responses);
        System.out.println("responses.getTotalPages() = " + responses.getTotalPages());

        System.out.println("paging = " + paging);
        System.out.println("responses.getContent() = " + responses.getContent());
        System.out.println("boardSearchDto = " + boardSearchDto);

        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("responses", responses);
        model.addAttribute("paging", paging);
        return "home";
    }

    @GetMapping("/paging")
    @ResponseBody
    public Page<BoardResponseDto> main2(Model model,
                                        @PageableDefault(page = 0, size = 5, sort = "board_createdTime", direction = Sort.Direction.DESC)
                                        Pageable pageable, BoardSearchDto boardSearchDto) {
        Page<Board> boards = boardService.findAllPaging(pageable, boardSearchDto);
        Page<BoardResponseDto> responses = boards.map(BoardResponseDto::new);

        return responses;
    }


}
