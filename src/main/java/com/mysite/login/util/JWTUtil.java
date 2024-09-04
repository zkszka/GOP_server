//package com.mysite.login.util;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Jwts;
//
//@Component
//public class JWTUtil {
//
//    @Value("${jwt.secret.key}")
//    private String secretKey;
//
//    @Value("${jwt.expiration.time}")
//    private long expirationTime; // in milliseconds
//
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//}
