package cos.blog;

import cos.blog.model.entity.Member;
import cos.blog.service.MemberService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }


    @Bean
    public InitService initService() {
        return new InitService();
    }


    public static class InitService {

        @Autowired
        MemberService memberService;

        @PostConstruct
        void initData() {
            for (int i = 1; i <= 100; i++) {
                memberService.join(new Member("Kim" + i, "aaa", "aaa"));
            }
        }
    }
}
