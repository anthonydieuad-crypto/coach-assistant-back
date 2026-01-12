package net.javaguide.coachassistant.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "MaSuperCleSecretePourLeCoachAssistantDeLaMortQuiTue";

    // ⏳ Durée de validité (ex: 24 heures en millisecondes)
    private static final long EXPIRATION_TIME = 86400000;

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 1. Générer le Token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // On stocke le rôle dedans !
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // 2. Récupérer l'email depuis le Token
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    // 3. Vérifier si le Token est valide
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token invalide : " + e.getMessage());
            return false;
        }
    }
}
