package cos.blog.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public AjaxAwareAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if (isAjax) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ajax REquest Denied (Session Expired)");
        } else {
            super.commence(request, response, authException);
        }

    }
}
