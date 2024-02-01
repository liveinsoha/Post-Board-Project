package cos.blog.web.controller.board.dto;

import cos.blog.web.controller.PagingDto;
import cos.blog.web.dto.BoardResponseDto;
import cos.blog.web.dto.ReplyResponseDto;
import org.springframework.data.domain.Page;

public class PagingReplyDto extends PagingDto {

    public PagingReplyDto(Page<ReplyResponseDto> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();
        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP; //5개 페이지씩 보여준다.

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses.getNumber());

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;
    }
}
