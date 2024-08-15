package org.example.allermiserver.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.allermiserver.global.util.AppProperties;
import org.example.allermiserver.global.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Component
public class LoginFilter implements Filter {
    private AppProperties properties = new AppProperties();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // 헤더에서 Authorization 값 가져오기
        String authHeader = httpRequest.getHeader("Authorization");

        if (path.equals("/auth/login") || path.equals("/auth/register")) {
            // 필터를 통과하고 다음 필터로 넘어감
            chain.doFilter(request, response);
            return;
        }


        if (authHeader == null || authHeader.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 에러
            httpResponse.getWriter().write("Authorization header is missing");
            return;
        }

        try {
            // SHA-256 해싱
            String hashedHeader = HashUtil.sha256(authHeader);

            // 사전 정의된 해시 값과 비교
            if (HashUtil.sha256(properties.getSecret()).equals(hashedHeader)) {
                // 인증 성공, 다음 필터 또는 서블릿으로 요청 전달
                chain.doFilter(request, response);
            } else {
                // 인증 실패, 401 에러
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid authorization token");
            }

        } catch (NoSuchAlgorithmException e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 에러
            httpResponse.getWriter().write("Internal server error");
        }
    }
}
