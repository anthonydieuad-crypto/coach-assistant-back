package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UtilisateurRepository utilisateurRepository,PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/Login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String passwordBrut = loginData.get("password");//mot de passe tapé par l'utilisateur
    //recherche de l'utilisateur
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        if (userOpt.isPresent()){
            Utilisateur user = userOpt.get();
            //CAS 1 : MDP sécurisé OK
            if (passwordEncoder.matches(passwordBrut, user.getPassword())) {
                return renvoyerInfosUser(user);
            }
            //CAS 2 : Ancien MDP en clair
            if (user.getPassword().equals(passwordBrut)) {
                user.setPassword(passwordEncoder.encode(passwordBrut));
                utilisateurRepository.save(user);
                return renvoyerInfosUser(user);
            }
        }
        //CAS 3 : Échec authentification
        return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur user) {
        //vérification que le mail existe déjà
        if (utilisateurRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Cet email est déja utilisé.");
        }
        //Chiffrage du MDP
        String motDePasseChiffre = passwordEncoder.encode(user.getPassword());
        user.setPassword(motDePasseChiffre);

        //Définition du rôle par défaut si non fournit par le Front
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("COACH");
        }

        //sauvegarde de l'utilisateur sécurisé
        Utilisateur savedUser = utilisateurRepository.save(user);
        //Connection de l'utilisateur avec ses infos
        return renvoyerInfosUser(savedUser);
    }

    private ResponseEntity<Map<String, Object>> renvoyerInfosUser(Utilisateur user) {
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole() != null ? user.getRole() : "COACH",
                "nom", user.getNom(),
                "prenom", user.getPrenom()
        ));
    }
}