package cos.blog.config.security.authInterceptor;

import cos.blog.config.security.PrincipalDetails;
import cos.blog.web.dto.BoardResponseDto;
import cos.blog.web.model.entity.Board;
import cos.blog.web.repository.board.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class BoardAuthInterceptor implements HandlerInterceptor {

    @Autowired
    BoardRepository boardRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String httpMethod = request.getMethod();
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("principal = " + principal);

        if (httpMethod.equals("GET") || httpMethod.equals("POST") || httpMethod.equals("DELETE")) {


            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println("pathVariables = " + pathVariables);
            Long boardId = Long.parseLong((String) pathVariables.get("boardId"));

            Board board = boardRepository.findById(boardId).get(); //id는 가지고 있으므로 지연로딩 하지 않는다.


            if (!Objects.equals(principal.getMember().getId(), board.getMember().getId())){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"권한이 없습니다");
                return false;
            }
            log.info("수정 권한 확인");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}