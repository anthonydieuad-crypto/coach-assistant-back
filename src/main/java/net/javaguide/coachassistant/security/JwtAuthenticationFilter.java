package net.javaguide.coachassistant.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UtilisateurRepository utilisateurRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UtilisateurRepository utilisateurRepository) {
        this.jwtUtils = jwtUtils;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupérer le token dans le header "Authorization"
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Enlever "Bearer "

            // 2. Vérifier la validité
            if (jwtUtils.validateToken(token)) {
                String email = jwtUtils.getEmailFromToken(token);

                // 3. Charger l'utilisateur depuis la BDD (pour être sûr qu'il existe encore)
                Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    Utilisateur user = userOpt.get();

                    // 4. Créer l'objet d'authentification officiel Spring
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
                    );

                    // 5. Le mettre dans le contexte (Le laisser passer !)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}