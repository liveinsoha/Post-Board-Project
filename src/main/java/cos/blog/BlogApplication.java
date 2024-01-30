package cos.blog;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import cos.blog.web.model.entity.Board;
import cos.blog.web.model.entity.Member;
import cos.blog.web.service.BoardService;
import cos.blog.web.service.MemberService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }


    @Bean
    public InitService initService() {
        return new InitService();
    }

    /**
     * 프록시 객체를 로딩하기 위한 라이브러리
     * @return
     */
    @Bean
    Hibernate5JakartaModule hibernate5Module() {
        Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
        hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5JakartaModule;
    }

    public static class InitService {

        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        MemberService memberService;

        @Autowired
        BoardService boardService;

        @PostConstruct
        void initData() {
            for (int i = 1; i <= 10; i++) {
                Member member = new Member("kimkim" + i, bCryptPasswordEncoder.encode("aaa"), "aaa");
                Long memberId = memberService.join(member);
                for (int j = 1; j <= 3; j++) {
                    Long boardId = boardService.addBoard("title" + j, "content", member);
                }

            }
        }
    }
}
