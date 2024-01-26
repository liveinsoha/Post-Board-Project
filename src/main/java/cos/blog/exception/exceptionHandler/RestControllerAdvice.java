package cos.blog.exception.exceptionHandler;


import cos.blog.exception.ErrorResult;
import cos.blog.exception.NoSuchMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@org.springframework.web.bind.annotation.RestControllerAdvice(basePackages = "cos.blog.test")
public class RestControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<ErrorResult> handlerNoSuchMemberEx(NoSuchMemberException noSuchmemberException) {
        ErrorResult errorResult = new ErrorResult("BAD", noSuchmemberException.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
