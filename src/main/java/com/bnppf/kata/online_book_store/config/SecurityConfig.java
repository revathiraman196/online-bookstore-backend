package com.bnppf.kata.online_book_store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${auth.username}")
    private String username;

    @Value("${auth.password}")
    private String password;

    @Value("${allowed-origin}")
    private String allowedOrigin;

    private static final String[] AUTH_WHITELIST = {"/", "/index.html", "/static/**", "/*.js", "/*.css", "/*.png", "/*.jpg", "/*.gif","/*.woff","/*.svg", "/*.ico","/webapp/**","/assets/**"};
    private static final String[] ALLOWED_METHODS={"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    private static final String ROLE="ADMIN";
    Logger LOG= LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type","charset"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfiguration -> corsConfiguration.configurationSource(corsConfigurationSource()) )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) ->
                {
                    LOG.info("Unauthorized request: " + request.getRequestURI()+request.getHeader("Authorization"));
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        LOG.debug("username {} :: password {}",username,password);
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(username)
                .password(passwordEncoder().encode(password))
                .roles(ROLE)
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}