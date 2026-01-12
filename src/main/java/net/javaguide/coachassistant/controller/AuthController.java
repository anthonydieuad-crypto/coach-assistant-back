package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.javaguide.coachassistant.security.JwtUtils;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils; // 👈 Injection

    public AuthController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String passwordBrut = loginData.get("password");

        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            // Vérification mot de passe
            if (passwordEncoder.matches(passwordBrut, user.getPassword()) || user.getPassword().equals(passwordBrut)) {

                // Si vieux mot de passe en clair, on met à jour
                if (user.getPassword().equals(passwordBrut)) {
                    user.setPassword(passwordEncoder.encode(passwordBrut));
                    utilisateurRepository.save(user);
                }

                // 👇 GÉNÉRATION DU TOKEN
                String token = jwtUtils.generateToken(user.getEmail(), user.getRole());

                // On renvoie User + Token
                return renvoyerInfosUser(user, token);
            }
        }
        return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
    }

    // Ajoute aussi la génération de token dans le /register si tu veux connecter direct après inscription

    // Modifie la méthode privée pour inclure le token
    private ResponseEntity<Map<String, Object>> renvoyerInfosUser(Utilisateur user, String token) {
        return ResponseEntity.ok(Map.of(
                "token", token, // 👈 LE PRÉCIEUX !
                "id", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole() != null ? user.getRole() : "COACH",
                "nom", user.getNom(),
                "prenom", user.getPrenom()
        ));
    }
}