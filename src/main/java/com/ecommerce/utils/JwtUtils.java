package com.ecommerce.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecommerce.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.ecommerce.decorators.JwtUser;
import com.ecommerce.decorators.RequestSession;
import com.ecommerce.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@RequiredArgsConstructor
@Component
public class JwtUtils {

    private final RequestSession requestSession;
    private static final String SECRET_KEY = "qp7R3rJ0FLr4aI5n42B0K3Qzg2ZHW+kfwLMmi5CPEfB8uTiZDCuoUUhBPq0hnx5g";

    public void extractToken(String token){
        JwtUser jwtUser = new JwtUser();
        Claims claims = extractAllClaims(token);
        jwtUser.setUserId(claims.get("userId", String.class));
        jwtUser.setRole((List<Role>) claims.get("role"));
        jwtUser.setIssuedTime(claims.get("issuedTime", Date.class));
        jwtUser.setExpirationTime(claims.get("expirationTime", Date.class));
        requestSession.setJwtUser(jwtUser);
    }

    public boolean isTokenExpired() {
        return requestSession.getJwtUser().getExpirationTime().before(new Date());
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(User user){
        return generateToken(new HashMap<>(),user);
    }
    public String generateToken(Map<String, Object> claims,User user) {
        claims.put("userId",user.getId());
        claims.put("role",user.getRoles());
        claims.put("issuedTime",new Date(System.currentTimeMillis()));
        claims.put("expirationTime",new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
