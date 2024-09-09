package bssm.bsmauth.global.http;

import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CustomRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest((HttpServletRequest) req);
        chain.doFilter(multiReadHttpServletRequest, res);
    }

}