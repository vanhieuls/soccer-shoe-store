package com.dailycodework.shopping_cart.Configuration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    String secretKey;
    @Value("${security.jwt.token.expiration}")
    long expirationDate;
    @Value("${security.jwt.token.expiration_refresh}")
    long expirationRefreshDate;
    public String generateToken(UserDetails userDetails){
        return deloyGenerateToken(userDetails,expirationDate);
    }
    public String generateRefreshToken(UserDetails userDetails){
        return deloyGenerateToken(userDetails,expirationRefreshDate);
    }
    public String deloyGenerateToken(UserDetails userDetails, long expireTime){
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expireDate = new Date(new Date().getTime()+expireTime);
        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) //id token: Dùng để thu hồi khi cần
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }
    private Key key(){
        byte[] bytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    public Date getExpiryTime(String token){
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiryTime = claims.getExpiration();
        return expiryTime;
    }
    public boolean isTokenValid(String token){
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  claims.getExpiration().after(Date.from(Instant.now()));
    }
    public String extractUsername (String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()) //// Lấy key từ phương thức key()
                    .build()
                    //Giải mã token và kiểm tra các thông tin như hết hạn (expiration), tính hợp lệ của cấu trúc.
                    .parse(token);//🔹 Giải mã token. Có thể giải mã cả JWS (signed JWT) lẫn JWT không có chữ ký (unsigned JWT).
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
            throw new RuntimeException(e); // 🔥 Token không hợp lệ
        }
    }
// Tức là JJWT tự kiểm tra hết hạn và các lỗi bảo mật, bạn không cần kiểm tra thủ công nữa.
//            //Nếu token:
////hết hạn → ném ExpiredJwtException
////
////sai chữ ký → ném SignatureException
////
////token lỗi định dạng → MalformedJwtException
////
////null hoặc không parse được → IllegalArgumentException
}
