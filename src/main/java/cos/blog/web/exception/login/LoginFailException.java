package cos.blog.web.exception.login;

import org.springframework.security.core.AuthenticationException;

public class LoginFailException extends AuthenticationException {

    public LoginFailException() {
        super("아이디와 비밀번호를 확인해주세요");
    }
}
