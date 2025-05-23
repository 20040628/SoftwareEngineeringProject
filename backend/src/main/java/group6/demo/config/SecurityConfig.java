package group6.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Value("${app.test-mode:false}")
    private boolean testMode;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable());
            
        if (testMode) {
            // 测试模式下，允许特定API无需认证
            http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/avatar/test-base64").permitAll()
                .requestMatchers("/api/admin/migration/**").permitAll()
                .requestMatchers("/api/scooters/add").hasRole("ADMIN")
                .requestMatchers("/api/scooters/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/scooters/update/**").hasRole("ADMIN")
                .requestMatchers("/api/scooters/getAll", "/api/scooters/{id}").permitAll()
                .requestMatchers("/api/feedback/all").hasRole("ADMIN")
                .requestMatchers("/api/feedback/**").authenticated()
                .requestMatchers("/api/bookings/forUnregistered").hasRole("ADMIN")
                .requestMatchers("/api/bookings/getAll").hasRole("ADMIN")
                .requestMatchers("/api/bookings/admin/return").hasRole("ADMIN")
                .requestMatchers("/api/bookings/**").authenticated()
                .requestMatchers("/api/users/changeStatus/{id}").hasRole("ADMIN")
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/alipay/**").authenticated()
                .requestMatchers("/api/stores/**").authenticated()
                .anyRequest().authenticated()
            );
        } else {
            // 正常模式
            http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/scooters/add").hasRole("ADMIN")
                .requestMatchers("/api/scooters/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/scooters/update/**").hasRole("ADMIN")
                .requestMatchers("/api/scooters/getAll", "/api/scooters/{id}").permitAll()
                .requestMatchers("/api/feedback/all").hasRole("ADMIN")
                .requestMatchers("/api/feedback/**").authenticated()
                .requestMatchers("/api/bookings/forUnregistered").hasRole("ADMIN")
                .requestMatchers("/api/bookings/getAll").hasRole("ADMIN")
                .requestMatchers("/api/bookings/admin/return").hasRole("ADMIN")
                .requestMatchers("/api/bookings/**").authenticated()
                .requestMatchers("/api/users/changeStatus/{id}").hasRole("ADMIN")
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/alipay/**").authenticated()
                .requestMatchers("/api/stores/**").authenticated()
                .anyRequest().authenticated()
            );
        }
            
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"No access authorization\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"error\": \"permission denied\"}");
                })
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5174",
            "http://118.24.22.77"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 