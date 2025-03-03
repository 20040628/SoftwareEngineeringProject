package group6.demo.config;

import group6.demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        Long userId = null;
        Integer userType = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                userId = jwtUtil.extractUserId(jwt);
                userType = jwtUtil.extractUserType(jwt);
            } catch (Exception e) {
                // Invalid token, continue to next filter
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Set user authentication
            List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
            );
            
            if (userType != null && userType == 1) {
                authorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_ADMIN")
                );
            }
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorities);
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
} 