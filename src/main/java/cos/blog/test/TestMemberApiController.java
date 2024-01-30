package cos.blog.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class TestMemberApiController {


    @GetMapping("/ajaxTest")
    public String ajaxTest() {
        return "/test/ajaxTest";
    }

    @PostMapping("/members/search")
    public String searchMembers(Model model) {

        log.info("요청 들어옴");

        TestMember testMember1 = new TestMember("qqqq", 10);
        TestMember testMember2 = new TestMember("wwww", 20);
        TestMember testMember3 = new TestMember("eeee", 30);
        TestMember testMember4 = new TestMember("wwaa", 40);

        List<TestMember> testMembers = Arrays.asList(testMember1, testMember2, testMember3, testMember4);

        model.addAttribute("members", testMembers);

        return "/test/member-list";
    }
}
