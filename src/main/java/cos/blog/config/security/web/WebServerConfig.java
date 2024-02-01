package cos.blog.config.security.web;

import cos.blog.config.security.authInterceptor.BoardAuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebServerConfig implements WebMvcConfigurer {


    @Autowired
    BoardAuthInterceptor boardAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("BoardAuthInterceptor 등록");

        registry.addInterceptor(boardAuthInterceptor)
                .addPathPatterns("/board/edit/**", "/member/edit");

    }
}

