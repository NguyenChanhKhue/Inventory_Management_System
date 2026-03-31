package com.Khue.InventoryMgtSystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter { // moi request di vao sever , AuthFilter se chay 1 lan duy nhat 

    private final JwtUtils jwtUtils;
    private final CustomUserDetailService customUserDetailService;;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request); // lay token 
        
        // check token , lay info cua token , ... 
        if (token != null) {
            String email = jwtUtils.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(email); // lay thong tin user tu DB

            if (StringUtils.hasText(email) && jwtUtils.isTokeValid(token, userDetails)) {
                log.info("Valid Token, {}", email);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // cho request xuong Filter tiep theo hoac controller , hoac bat exception
        try { 
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception occurred in AuthFilter: " + e.getMessage());
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}