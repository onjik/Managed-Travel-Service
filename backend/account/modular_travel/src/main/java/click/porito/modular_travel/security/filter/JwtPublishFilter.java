package click.porito.modular_travel.security.filter;

import click.porito.modular_travel.security.dto.JwtPayload;
import click.porito.modular_travel.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 요청이 올때 Authorization 헤더가 없었고, 응답이 나갈때, SecurityContextHolder가 존재하면,
 * JWT를 만들어서 응답에 실어서 보내주는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtPublishFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean unauthorizedRequest = SecurityContextHolder.getContext().getAuthentication() == null;
        filterChain.doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //refresh 토큰, access 토큰 발급, refresh 토큰 재설정
        if (unauthorizedRequest && authentication != null){
            //이번 요청 중에 인증이 되었다면, 응답에 JWT를 실어서 보내준다.
            boolean committed = response.isCommitted();
            if (committed){
                //응답이 이미 나갔다면, JWT를 실어서 보낼 수 없다.
                log.warn("response is already committed, cannot publish jwt");
                return;
            }
            //jwt 발급
            List<String> roleNames = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            JwtPayload payload = new JwtPayload(authentication.getName(), roleNames);
            String token = jwtService.encode(payload);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }


    }
}
