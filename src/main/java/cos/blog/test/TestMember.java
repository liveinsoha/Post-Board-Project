package cos.blog.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@RequiredArgsConstructor
public class TestMember {

    private final String name;
    private final int age;

}
