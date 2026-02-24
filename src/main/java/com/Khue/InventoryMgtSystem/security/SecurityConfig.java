package com.Khue.InventoryMgtSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Khue.InventoryMgtSystem.exceptions.CustomAccessDenialHandler;
import com.Khue.InventoryMgtSystem.exceptions.CustomAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final AuthFilter authFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDenialHandler customAccessDenialHandler;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // không dùng session , cookie , dùng JWT để xác thực
        .cors(Customizer.withDefaults())
        .exceptionHandling(exception -> exception
            .accessDeniedHandler(customAccessDenialHandler) // chặn user đă login nhưng chưa đủ quyền
            .authenticationEntryPoint(customAuthenticationEntryPoint)) // chặn user chưa login mà access API protected
        .authorizeHttpRequests(request -> request
            .requestMatchers("/api/auth/**").permitAll() // cho phép tất cả user đều vào được API này
            .anyRequest().authenticated() // tất cả API khác thì không
        )
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // không lưu session, mỗi req phải được authenticae
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Run authFilter trước khi Spring Login , để xử lý JWT

        return httpSecurity.build();                
    }


    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager(); // dùng để xác thực username và pw. trả về 1 token gồm (userdetail ,...)
    }
}
