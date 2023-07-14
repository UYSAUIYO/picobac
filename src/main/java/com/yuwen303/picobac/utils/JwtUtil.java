package com.yuwen303.picobac.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author YuWen
 */
@Component
public class JwtUtil {
    private final String secret = "ndjq9w00d9w";
    private final long expire = 7 * 24 * 60 * 60 * 1000;

    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public String getUsernameFromToken(String token) {
        try{
            Claims claims = Jwts.parser ()
                    .setSigningKey (secret)
                    .parseClaimsJws (token)
                    .getBody ();
            return claims.getSubject ();
        }catch (Exception e){
            return e.getMessage ();
        }

    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 处理异常
        }
        return false;
    }
}
