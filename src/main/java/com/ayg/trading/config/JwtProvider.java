package com.ayg.trading.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;


public class JwtProvider {
    private static SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public static String createToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);
        return Jwts.builder()
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(86400)))
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(secretKey)
                .compact();
    }


    public static String getEmailFromToken(String token) {
        token = token.replaceFirst("Bearer ", "");
        Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return String.valueOf(claims.get("email"));
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        authorities.forEach(authority -> authoritiesSet.add(authority.getAuthority()));
        return String.join(",", authoritiesSet);
    }
}
