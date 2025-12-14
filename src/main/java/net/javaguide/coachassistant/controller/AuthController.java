package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import net.javaguide.coachassistant.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final EmailService emailService;

    public AuthController(UtilisateurRepository utilisateurRepository, EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        return utilisateurRepository.findByEmail(email)
                .map(user -> {
                    if (user.getPassword().equals(password)) {
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

    // 👇 NOUVELLE ROUTE D'INSCRIPTION
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur user) {
        if (utilisateurRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Cet email est déjà utilisé !");
        }

        if (user.getRole() == null) user.setRole("COACH");

        Utilisateur savedUser = utilisateurRepository.save(user);

        // 📧 Envoi de l'alerte
        try {
            emailService.envoyerAlerteNouveauCoach(savedUser);
        } catch (Exception e) {
            System.err.println("Erreur envoi mail : " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "id", savedUser.getId(),
                "email", savedUser.getEmail(),
                "nom", savedUser.getNom(),
                "prenom", savedUser.getPrenom()
        ));
    }
}