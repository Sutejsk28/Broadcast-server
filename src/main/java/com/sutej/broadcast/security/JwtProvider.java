package com.sutej.broadcast.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtProvider {
//
//    JWT jwt = JWTParser.parse();
//    Header = jwt.getHeader();
//    JWTClaimsSet jwtClaimSet = jwt.getJWTClaimsSet();
//
//
//    @Value("90000")
//    private Long jwtExpirationInMillis;
//
//    public String generatedToken(Authentication authentication){
//        User principal = (User) authentication.getPrincipal();
//        return generatedTokenWithUserName(principal.getUserName());
//    }
//
//    public String generatedTokenWithUserName(String username){
//        JWTClaimsSet claims = JWTClaimsSet.builds()
//                .issue("self")
//                .issuedAt(Instant.now())
//                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
//                .subject(username)
//                .claim("scope", "ROLE_USER")
//                .build();
//
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//
//        return null;
//    }

    private String SECRET_KEY = "secret";
//    @Value("${jwts.expiration.time}")
    private Long jwtExpirationInMillis = Instant.now().getEpochSecond();

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope","ROLE_USER" );
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTokenWithUserName(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope","ROLE_USER" );
        return createToken(claims, username);

    }


    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
