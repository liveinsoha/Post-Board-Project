package cos.blog.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String defaultUrl = "/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        super.clearAuthenticationAttributes(request);//에러세션 지운다


        SavedRequest savedRequest = requestCache.getRequest(request, response);

        resultRedirectStrategy(request, response, authentication); //Redirect URL작업
        requestCache.removeRequest(request, response);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        HttpSession session = request.getSession();
        String returnUrl = session.getAttribute("returnUrl") == null ? "" : session.getAttribute("returnUrl").toString();
        session.removeAttribute("returnUrl");

        if (!returnUrl.equals("")) {
            String url = returnUrl;
            log.info("returnUrl 있다 = {}", returnUrl);
            getRedirectStrategy().sendRedirect(request, response, url);
            return;
        }

        if (savedRequest != null) {

            log.debug("권한이 필요한 페이지에 접근했을 경우");
            useSessionUrl(request, response);
        } else {
            log.debug("직접 로그인 url로 이동했을 경우");
            useDefaultUrl(request, response);
        }
    }

    protected void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectStrategy.sendRedirect(request, response, defaultUrl);
    }

}
