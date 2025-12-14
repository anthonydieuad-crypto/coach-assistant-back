package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.entity.ScoreJongle;
import net.javaguide.coachassistant.service.JoueurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/joueurs")
@CrossOrigin("*")
public class JoueurController {

    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    // GET /api/joueurs?coachId=1
    @GetMapping
    public List<Joueur> getAllJoueurs(@RequestParam(required = false) Long coachId) {
        return joueurService.recupererJoueursDuCoach(coachId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joueur> getJoueurById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(joueurService.recupererJoueurParId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/joueurs?coachId=1
    @PostMapping
    public Joueur createJoueur(@RequestBody Joueur joueur, @RequestParam(required = false) Long coachId) {
        return joueurService.sauvegarderJoueur(joueur, coachId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joueur> updateJoueur(@PathVariable Long id, @RequestBody Joueur joueur) {
        joueur.setId(id);
        // Pour une mise à jour, on garde le coach existant (géré par le service/JPA)
        return ResponseEntity.ok(joueurService.sauvegarderJoueur(joueur, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoueur(@PathVariable Long id) {
        joueurService.supprimerJoueur(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/jongles")
    public Joueur addScoreJongle(@PathVariable Long id, @RequestBody ScoreJongle score) {
        return joueurService.ajouterScoreJongle(id, score.getDate(), score.getScore());
    }

    @PostMapping("/{id}/presence")
    public Joueur togglePresence(@PathVariable Long id, @RequestParam LocalDate date) {
        return joueurService.basculerPresence(id, date);
    }
}