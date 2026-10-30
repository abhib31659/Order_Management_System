package com.ecommerce.user_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String email){

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + jwtExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    /// /After login for every request we need to verify the jwt token for authorization and to verify the request is of genuine user or not if yes then check his authorization for the request and is he's authorized to that then allow his request
    public String extractUsername(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, String email){

        String extractedEmail = extractUsername(token);

        return extractedEmail.equals(email)
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){

        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }
}
