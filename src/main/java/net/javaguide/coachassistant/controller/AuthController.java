package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Autorise Angular
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Route de connexion
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        return utilisateurRepository.findByEmail(email)
                .map(user -> {
                    if (user.getPassword().equals(password)) {
                        // Connexion réussie : on renvoie l'utilisateur (sans le mot de passe par sécurité)
                        return ResponseEntity.ok(Map.of(
                                "id", user.getId(),
                                "email", user.getEmail(),
                                "role", user.getRole(),
                                "nom", user.getNom(),
                                "prenom", user.getPrenom()
                        ));
                    } else {
                        return ResponseEntity.status(401).body("Mot de passe incorrect");
                    }
                })
                .orElse(ResponseEntity.status(404).body("Utilisateur inconnu"));
    }
}
