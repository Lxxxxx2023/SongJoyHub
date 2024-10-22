package com.lx.SongJoyHub.client.util;

import com.alibaba.fastjson.JSON;
import com.lx.SongJoyHub.client.common.context.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT校验工具类
 */
@Slf4j
public final class JWTUtil {
    private static final long EXPIRATION = 64000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ISS = "SongJoyHub";
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * 生成用户token
     *
     * @param userInfo 用户信息
     * @return 用户访问Token
     */
    public static String generateToken(UserInfoDTO userInfo) {
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("userId", userInfo.getUserId());
        userInfoMap.put("userRole", userInfo.getUserRole().getRole());
        userInfoMap.put("userName", userInfo.getUserName());
        userInfoMap.put("roomId", userInfo.getRoomId());
        String jwtToken = Jwts.builder()
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setIssuer(ISS)
                .setSubject(JSON.toJSONString(userInfoMap))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .compact();
        return TOKEN_PREFIX + jwtToken;
    }

    public static UserInfoDTO parseJwtToken(String jwtToken) {
        if (StringUtils.hasText(jwtToken)) {
            String actualJwtToken = jwtToken.replace(TOKEN_PREFIX, "");
            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(actualJwtToken).getBody();
                Date expiration = claims.getExpiration();
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    return JSON.parseObject(subject, UserInfoDTO.class);
                }
            } catch (ExpiredJwtException ignored) {
            }
            catch (Exception e) {
                log.error("JWT Token 解析失败，请检查", e);
            }
        }
        return null;
    }
}
