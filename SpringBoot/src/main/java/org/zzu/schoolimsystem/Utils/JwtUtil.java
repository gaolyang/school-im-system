package org.zzu.schoolimsystem.Utils;

/**
 * ClassName: JwtUtils
 * Package: org.zzu.schoolimsystem.Utils
 * Description:
 *
 * @Author gly
 * @Create 2026/3/17 9:14
 * @Version 1.0
 */
//package org.zzu.schoolimsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * 说明：
 * 1. secret 至少需要 32 个字符，推荐放在 application.yml 中配置。
 * 2. token 中保存 userId，供业务接口从 token 中解析当前用户。
 */
@Component
public class JwtUtil {

    /**
     * application.yml 中配置：
     * jwt:
     *   secret: your-very-long-secret-key-at-least-32-chars
     *   expiration: 86400000
     */
    @Value("${jwt.secret:schoolimsystem-jwt-secret-key-please-change-123456}")
    private String secret;

    /** token 有效期，默认 24 小时，单位毫秒 */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (secret == null || secret.trim().length() < 32) {
            throw new IllegalArgumentException("jwt.secret 长度不能少于 32 个字符");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 生成 token，可在登录成功时调用 */
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return generateToken(claims, String.valueOf(userId));
    }

    /** 生成 token（带额外 claims） */
    public String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /** 从 token 中解析 Claims */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("token 解析失败", e);
        }
    }

    /** 从 token 中获取 userId */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj == null) {
            throw new RuntimeException("token 中不存在 userId");
        }

        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        if (userIdObj instanceof String) {
            return Long.parseLong((String) userIdObj);
        }
        return Long.parseLong(String.valueOf(userIdObj));
    }

    /** 获取过期时间 */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /** 判断 token 是否过期 */
    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    /** 校验 token 是否有效 */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
