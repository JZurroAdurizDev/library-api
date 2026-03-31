package com.jabierzurro.libraryapi.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jabierzurro.libraryapi.security.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom security filter responsible for JWT-based authentication.
 *
 * <p>This filter is executed once per request and checks whether the incoming
 * HTTP request contains a valid JWT token in the {@code Authorization} header.
 *
 * <p>If a valid token is found:
 * <ul>
 *     <li>the token is validated,</li>
 *     <li>the username and authorities are extracted,</li>
 *     <li>an {@link Authentication} object is created,</li>
 *     <li>the authentication is stored in the {@link SecurityContext}.</li>
 * </ul>
 *
 * <p>If no token is present, or if the header does not start with
 * {@code Bearer }, the filter simply passes control to the next element
 * in the filter chain.
 *
 * @author Jabier Zurro Aduriz
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Constructor for injecting the JWT service used to validate and decode tokens.
     *
     * @param jwtService service responsible for token validation and claim extraction
     */
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Processes each HTTP request and attempts to authenticate the user
     * based on the JWT token provided in the {@code Authorization} header.
     *
     * <p>If the token is valid, the user's authentication is stored in the
     * {@link SecurityContextHolder}, allowing downstream security checks
     * to treat the request as authenticated.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param filterChain security filter chain
     * @throws ServletException if the request cannot be processed
     * @throws IOException if an input or output error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            DecodedJWT decodedJWT = jwtService.validateToken(jwtToken);
            String username = jwtService.extractUsername(decodedJWT);
            String authorities = jwtService.getSpecificClaim(decodedJWT, "authorities").asString();

            Collection<GrantedAuthority> authoritiesList =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}