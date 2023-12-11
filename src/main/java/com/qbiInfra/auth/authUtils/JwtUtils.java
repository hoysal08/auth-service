package com.qbiInfra.auth.authUtils;


import com.qbiInfra.auth.authUtils.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .claim("uid", user.getUid())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("isEmailVerified", user.isEmailVerified())
                .claim("issuer", user.getIssuer())
                .claim("picture", user.getPicture())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static User getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        User user = new User();
        user.setUid(claims.get("uid", String.class));
        user.setName(claims.get("name", String.class));
        user.setEmail(claims.get("email", String.class));
        user.setEmailVerified(claims.get("isEmailVerified", Boolean.class));
        user.setIssuer(claims.get("issuer", String.class));
        user.setPicture(claims.get("picture", String.class));

        return user;
    }

    public static boolean validateToken(String token) {
        try {
            // Parsing the token will throw an exception if it's not valid
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

