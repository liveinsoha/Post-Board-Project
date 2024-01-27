package cos.blog.web.exception.exceptionHandler;

import cos.blog.web.exception.ErrorResult;
import cos.blog.web.exception.NoSuchMemberException;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.web.bind.annotation.ControllerAdvice(basePackages = "cos.blog.view")
public class ControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchMemberException.class)
    public ModelAndView handlerNoSuchmemberEx(NoSuchMemberException noSuchmemberException) {
        ErrorResult errorResult = new ErrorResult("BAD", noSuchmemberException.getMessage());
        return new ModelAndView("/errorPage/nomember");
    }
}
