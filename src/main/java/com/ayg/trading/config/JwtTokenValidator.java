package com.ayg.trading.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(JwtConstant.HEADER_STRING);
        if (jwtToken != null) {
            jwtToken = jwtToken.replace(JwtConstant.TOKEN_PREFIX, "");
            try{
                SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();
                String email =String.valueOf(claims.get("email"));
                String authorities =String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                       null,
                        grantedAuthorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (RuntimeException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Invalid JWT token", e);
            }
        }
        filterChain.doFilter(request, response);

    }
}
