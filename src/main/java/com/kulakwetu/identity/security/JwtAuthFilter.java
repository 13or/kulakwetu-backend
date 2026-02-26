package com.kulakwetu.identity.security;

import com.kulakwetu.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims claims = jwtService.parse(token);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                if (roles != null) {
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }

                @SuppressWarnings("unchecked")
                List<String> permissions = claims.get("permissions", List.class);
                if (permissions != null) {
                    permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
                }

                var authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
