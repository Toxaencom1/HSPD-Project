package com.taxah.hspd.service.auth;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.enums.TokenType;
import com.taxah.hspd.exception.JwtRefreshExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.taxah.hspd.util.constant.Params.*;


@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @Value("${token.ttl.access}")
    private int accessTokenTTL;
    @Value("${token.ttl.refresh}")
    private int refreshTokenTTL;
    private final RefreshTokenService refreshTokenService;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put(ID, customUserDetails.getId());
            claims.put(TYPE, TokenType.ACCESS.name());
            claims.put(EMAIL, customUserDetails.getEmail());
            claims.put(ROLE, customUserDetails.getRoles());
            claims.put(AUTHORITIES, customUserDetails.getAuthorities());
        }
        return generateToken(claims, userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(TYPE, TokenType.REFRESH.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenTTL))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenTTL))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            if (e.getClaims() != null
                    && e.getClaims().get(TYPE, String.class) != null &&
                    e.getClaims().get(TYPE, String.class)
                            .equals(TokenType.REFRESH.name())) {
                refreshTokenService.deleteByRefreshToken(token);
                throw new JwtRefreshExpiredException(e.getMessage(), e);
            }
            return e.getClaims();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
