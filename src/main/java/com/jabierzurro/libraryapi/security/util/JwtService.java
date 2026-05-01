package com.jabierzurro.libraryapi.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Service responsible for JWT creation, validation and claim extraction.
 *
 * <p>This service centralizes all token-related operations used by the
 * authentication layer of the application.
 *
 * <p>Its responsibilities include:
 * <ul>
 *     <li>building signed JWT tokens for authenticated users,</li>
 *     <li>validating incoming tokens,</li>
 *     <li>extracting claims such as username or authorities,</li>
 *     <li>configuring the signing algorithm based on application properties.</li>
 * </ul>
 *
 * <p>The token configuration is externalized through properties such as:
 * <ul>
 *     <li>{@code security.jwt.secret}</li>
 *     <li>{@code security.jwt.secret.format}</li>
 *     <li>{@code security.jwt.issuer}</li>
 *     <li>{@code security.jwt.expiration}</li>
 * </ul>
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign and verify JWT tokens.
     */
    @Value("${security.jwt.secret}")
    private String secret;

    /**
     * Format of the secret key.
     *
     * <p>Supported values depend on the implementation. In this service,
     * {@code HEX} means the secret is interpreted as a hexadecimal string.
     */
    @Value("${security.jwt.secret.format}")
    private String secretFormat;

    /**
     * Expected token issuer.
     */
    @Value("${security.jwt.issuer}")
    private String issuer;

    /**
     * Token expiration time in milliseconds.
     */
    @Value("${security.jwt.expiration}")
    private long expiration;

    /**
     * Builds the signing algorithm used for JWT creation and validation.
     *
     * <p>If the configured secret format is {@code HEX}, the secret is decoded
     * from hexadecimal before building the algorithm. Otherwise, the secret
     * is used as a plain string.
     *
     * @return configured {@link Algorithm} for HMAC signing
     */
    private Algorithm getAlgorithm() {
        if ("HEX".equalsIgnoreCase(this.secretFormat)) {
            byte[] secretBytes = DatatypeConverter.parseHexBinary(this.secret);
            return Algorithm.HMAC256(secretBytes);
        }
        return Algorithm.HMAC256(this.secret);
    }

    /**
     * Creates a signed JWT token for an authenticated user.
     *
     * <p>The generated token includes:
     * <ul>
     *     <li>issuer,</li>
     *     <li>subject (username/email),</li>
     *     <li>authorities claim,</li>
     *     <li>issue date,</li>
     *     <li>expiration date,</li>
     *     <li>JWT identifier,</li>
     *     <li>not-before date.</li>
     * </ul>
     *
     * @param authentication authenticated user data provided by Spring Security
     * @return signed JWT token as a string
     */
    public String createToken(Authentication authentication) {
        Algorithm algorithm = getAlgorithm();

        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        return JWT.create()
                .withIssuer(this.issuer)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + this.expiration))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(now))
                .sign(algorithm);
    }

    /**
     * Extracts the username from a decoded JWT token.
     *
     * <p>In this application, the username is stored in the token subject.
     *
     * @param decodedJWT decoded JWT token
     * @return username stored in the token subject
     */
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }

    /**
     * Returns a specific claim from a decoded JWT token.
     *
     * @param decodedJWT decoded JWT token
     * @param claimName name of the claim to retrieve
     * @return requested {@link Claim}
     */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    /**
     * Returns all claims contained in a decoded JWT token.
     *
     * @param decodedJWT decoded JWT token
     * @return map containing all token claims
     */
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    /**
    * Validates a JWT token and returns its decoded representation.
    *
    * <p>The token is validated against:
    * <ul>
    *     <li>the configured signing algorithm,</li>
    *     <li>the expected issuer.</li>
    * </ul>
    *
    * @param token raw JWT token
    * @return validated and decoded {@link DecodedJWT}
    * @throws JWTVerificationException if the token is invalid, expired or cannot be verified
    */
    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(this.issuer)
                .build();

        return verifier.verify(token);
    }
}