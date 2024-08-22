package org.example.yourownex.service;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {
    private final SecretKey key;
    private final JwtParser parser;

    public JWTService() {
        key = new SecretKeySpec(Base64.getDecoder()
                .decode("P+HNMLncf7iP0K03AFhHuy5wZM0ngo+tPughJx8wd3w="), "HmacSHA256");
        parser = Jwts.parser().verifyWith(key).build();
    }

    public String getToken(Long userId) {
        return Jwts.builder()
                .expiration(DateUtils.addDays(new Date(), 7))
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public Long verify(String token) {
        return parser.parseSignedClaims(token).getPayload().get("userId", Long.class);
    }
}
