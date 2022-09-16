package com.de.nkoepf.backend.auth;

import com.de.nkoepf.backend.config.KeepTrackProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String AUTHORITIES = "authorities";
    private static final int TOKEN_START = 7;
    private final ObjectMapper mapper;


    private final KeepTrackProperties keepTrackProperties;

    public String withoutBearer(String token) {
        return token.substring(TOKEN_START);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        final Claims claims = extractAllClaims(token);
        List<LinkedHashMap<String, String>> auths = (ArrayList<LinkedHashMap<String, String>>) claims.get(AUTHORITIES);
        String role = auths.get(0).get("authority");
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    public String convertEntriesToString(List<String> entriesList) {
        String entries = entriesList.toString();
        entries = StringUtils.replace(entries, "[", "");
        entries = StringUtils.replace(entries, "]", "");
        return entries;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserTokenData userTokenData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(FIRST_NAME, userTokenData.getName());
        claims.put(LAST_NAME, userTokenData.getSurName());
        claims.put(AUTHORITIES, userTokenData.getAuthorities());
        return createToken(claims, userTokenData.getEmail());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder()
                .decode(keepTrackProperties.getJwtSecret()), SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(keepTrackProperties.getJwtExpirationMs(), ChronoUnit.MILLIS)))
                .signWith(hmacKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder()
                .decode(keepTrackProperties.getJwtSecret()), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
