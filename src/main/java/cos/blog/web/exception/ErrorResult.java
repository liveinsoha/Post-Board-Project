package cos.blog.web.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResult {

    private final String code;
    private final String message;
}
