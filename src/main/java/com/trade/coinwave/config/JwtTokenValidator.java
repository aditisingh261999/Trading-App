package com.trade.coinwave.config;

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

import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(JwtConstant.HEADER_STRING);

        if (jwtToken != null && jwtToken.startsWith(JwtConstant.TOKEN_PREFIX)) {
            jwtToken = jwtToken.replace(JwtConstant.TOKEN_PREFIX, "");

            try {
                String key = String.valueOf(Keys.hmacShaKeyFor(JwtConstant.SECRET.getBytes()));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String email = String.valueOf(claims.get("email")); // extracting email
                String authorities = String.valueOf(claims.get("authorities"));  // extracting roles

                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        grantedAuthorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
