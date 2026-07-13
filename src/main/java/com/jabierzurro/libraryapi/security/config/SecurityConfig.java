package com.jabierzurro.libraryapi.security.config;

import com.jabierzurro.libraryapi.security.filter.JwtAuthFilter;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Central security configuration for the application.
 *
 * <p>This class defines the Spring Security infrastructure used by the API,
 * including:
 * <ul>
 *     <li>the security filter chain,</li>
 *     <li>public and protected endpoints,</li>
 *     <li>stateless session management,</li>
 *     <li>password encoding,</li>
 *     <li>authentication provider configuration,</li>
 *     <li>JWT filter integration,</li>
 *     <li>method-level authorization using {@code @PreAuthorize} annotations.</li>
 * </ul>
 *
 * <p>The application uses JWT-based authentication, so sessions are configured
 * as stateless and the custom {@link JwtAuthFilter} is added before the standard
 * username/password authentication filter.
 *
 * <p>Method-level authorization is enabled via {@link EnableMethodSecurity},
 * allowing fine-grained access control using annotations such as
 * {@code @PreAuthorize}.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor for injecting security dependencies.
     *
     * @param jwtAuthFilter custom JWT authentication filter
     * @param userDetailsService service used to load users from the persistence layer
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Defines the main security filter chain of the application.
     *
     * <p>This configuration:
     * <ul>
     *     <li>disables CSRF protection for the stateless API,</li>
     *     <li>sets session management to {@code STATELESS},</li>
     *     <li>allows unauthenticated access to public endpoints,</li>
     *     <li>requires authentication for all other requests,</li>
     *     <li>registers the custom authentication provider,</li>
     *     <li>adds the JWT filter before Spring Security's standard authentication filter.</li>
     * </ul>
     *
     * <p>Role-based authorization is handled at method level using
     * {@code @PreAuthorize} annotations.
     *
     * @param http {@link HttpSecurity} object used to configure web-based security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if the security configuration cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {

        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                    PathPatternRequestMatcher.withDefaults().matcher("/"),
                    PathPatternRequestMatcher.withDefaults().matcher("/auth/login"),
                    PathPatternRequestMatcher.withDefaults().matcher("/auth/logout"),
                    PathPatternRequestMatcher.withDefaults().matcher("/auth/register"),
                    PathPatternRequestMatcher.withDefaults().matcher("/health"),
                    PathPatternRequestMatcher.withDefaults().matcher("/info")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(daoAuthenticationProvider())
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    /**
     * Creates the {@link DaoAuthenticationProvider} used to authenticate users
     * against the application's persistence layer.
     *
     * <p>This provider delegates user lookup to {@link UserDetailsServiceImpl}
     * and password verification to the configured {@link PasswordEncoder}.
     *
     * @return configured {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider dAuthP = new DaoAuthenticationProvider(userDetailsService);
        dAuthP.setPasswordEncoder(passwordEncoder());
        return dAuthP;
    }

    /**
     * Defines the password encoder used by the authentication provider.
     *
     * <p>The application uses BCrypt to hash and verify user passwords.
     *
     * @return configured {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the {@link AuthenticationManager} provided by Spring Security.
     *
     * <p>This bean is used by the authentication service to perform
     * username/password authentication during login.
     *
     * @param cfg authentication configuration provided by Spring Security
     * @return configured {@link AuthenticationManager}
     * @throws Exception if the authentication manager cannot be obtained
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of(
                "http://localhost:4200",
                "https://www.virtuallibrary.dev",
                "https://virtuallibrary.dev"
            )
        );

        configuration.setAllowCredentials(true);

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type"
            )
        );

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}