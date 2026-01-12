package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    private final UtilisateurRepository utilisateurRepository;

    public AdminController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // 1. Lister tous les utilisateurs (Pour ton Dashboard Admin)
    @GetMapping("/users")
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    // 2. Supprimer un utilisateur (Coach ou autre)
    // Grâce à l'étape 1, cela supprimera aussi ses joueurs et événements !
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
