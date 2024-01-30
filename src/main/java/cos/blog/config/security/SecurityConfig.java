package cos.blog.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final int ONE_MONTH = 2678400;
    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable); //SecurityConfig파일 작동 안하여 lonin을 이제 낚아채지 않는다.

        http.authorizeHttpRequests(authorize ->
                                authorize.requestMatchers("/board/createBoard","/board/reply/**").hasAnyRole("MEMBER", "ADMIN") //인증만 되면 둘어갈 수 있는 주소다
                                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                                        .requestMatchers("/manager/**").hasAnyRole("MEMBER")
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .anyRequest().permitAll()

                        //권한이 없는 경로로 접근할 때 로그인(/login) 페이지로 이동하고 싶다.
                        //3개를 제외하면 아무 권한 없이 접근이 가능하다.
                        //접근권한이 없는 경우 403이 발생
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("name") //html의 name속성 쿼리파라미터 요청,,
                                .passwordParameter("password") //
                                .successHandler(new LoginSuccessHandler())
                                .failureHandler(new LoginFailHandler())
                                .permitAll()

                ).rememberMe(customizer -> customizer
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(ONE_MONTH)
                        .userDetailsService(principalDetailsService)
                        .authenticationSuccessHandler(new LoginSuccessHandler())
                ).logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("remember-me")
                        .permitAll()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(ajaxAwareAuthenticationEntryPoint("/login")));

        return http.build();
    }

    private AjaxAwareAuthenticationEntryPoint ajaxAwareAuthenticationEntryPoint(String url) {
        return new AjaxAwareAuthenticationEntryPoint(url);
    }

}
