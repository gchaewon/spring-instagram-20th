package com.ceos20.instagram.global.config.jwt;

import com.ceos20.instagram.domain.user.domain.CustomUserDetailsService;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.global.ResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Builder
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            // 필터 내에서 직접 응답 처리
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");

            String message = "JWT 토큰이 유효하지 않거나 누락되었습니다.";
            ResponseTemplate<?> responseTemplate = ResponseTemplate.builder()
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .success(false)
                    .message(message)
                    .data(null)
                    .build();

            // ObjectMapper를 사용해서 json으로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage = objectMapper.writeValueAsString(responseTemplate);
            httpResponse.getWriter().write(responseMessage);

            return;
        }


        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 username을 추출하고, username으로 userId를 조회
            String username = jwtTokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // username으로 조회
            Long userId = ((User) userDetails).getId();  // userId 추출

            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            Authentication authenticated = new UsernamePasswordAuthenticationToken(
                    userId,  // userId를 Principal로 설정
                    "",
                    authentication.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }

        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 이후의 토큰을 반환
        }
        return null;
    }
}
