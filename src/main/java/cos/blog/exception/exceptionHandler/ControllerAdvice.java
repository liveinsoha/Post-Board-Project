package cos.blog.exception.exceptionHandler;

import cos.blog.exception.ErrorResult;
import cos.blog.exception.NoSuchMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.web.bind.annotation.ControllerAdvice(basePackages = "cos.blog.view")
public class ControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchMemberException.class)
    public ModelAndView handlerNoSuchmemberEx(NoSuchMemberException noSuchmemberException) {
        ErrorResult errorResult = new ErrorResult("BAD", noSuchmemberException.getMessage());
        return new ModelAndView("/errorPage/nomember");
    }
}
