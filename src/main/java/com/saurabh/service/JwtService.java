package com.saurabh.service;

import com.saurabh.enums.TokenType;
import com.saurabh.exception.TokenException;
import com.saurabh.exception.UserNotFoundException;
import com.saurabh.model.Token;
import com.saurabh.model.User;
import com.saurabh.repository.TokenRepository;
import com.saurabh.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String AUTHORITIES_CLAIM = "roles";
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String,
                    Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(
                extraClaims,
                userDetails,
                jwtExpiration);
    }

    public Token createRefreshToken(Long userId) {
        Token refreshToken = new Token();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setTokenType(TokenType.Bearer);
        refreshToken.setExpired(false);
        refreshToken.setRevoked(false);

        refreshToken = tokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Token verifyRefreshExpiration(Token token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new TokenException(token.getToken() + "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return tokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.setIssuedAt(new Date(System.currentTimeMillis()));
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration));
        claims.put(AUTHORITIES_CLAIM, getUserAuthorities(userDetails));

        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes =
                Decoders
                        .BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public List<GrantedAuthority> getAuthoritiesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // Get the roles claim as a comma-separated string
        String rolesClaim = claims.get(AUTHORITIES_CLAIM, String.class);

        if (rolesClaim != null) {
            return Arrays.stream(rolesClaim.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
    private String getUserAuthorities(UserDetails user) {
        return user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
