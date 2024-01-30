package cos.blog.web.controller;

import cos.blog.web.dto.BoardResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.service.BoardService;
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
public class HomeController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/")
    public String main1(Model model,
                        @PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
                        Pageable pageable) {
        Page<Board> boards = boardService.findAllPaging(pageable);
        Page<BoardResponseDto> boardFormDtos = boards.map(BoardResponseDto::new);
        model.addAttribute("boards", boardFormDtos);
        return "home";
    }

    @GetMapping("/paging")
    @ResponseBody
    public Page<Board> main2(Model model,
                             @PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
                             Pageable pageable) {
        Page<Board> boards = boardService.findAllPaging(pageable);

        return boards;
    }


}
