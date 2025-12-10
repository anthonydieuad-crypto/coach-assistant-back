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
@CrossOrigin("*") // Autorise Angular à nous parler
public class JoueurController {

    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    // GET /api/joueurs : Récupérer tous les joueurs
    @GetMapping
    public List<Joueur> getAllJoueurs() {
        return joueurService.recupererTousLesJoueurs();
    }

    // GET /api/joueurs/{id} : Récupérer un joueur spécifique
    @GetMapping("/{id}")
    public ResponseEntity<Joueur> getJoueurById(@PathVariable Long id) {
        return ResponseEntity.ok(joueurService.recupererJoueurParId(id));
    }

    // POST /api/joueurs : Créer un nouveau joueur
    @PostMapping
    public Joueur createJoueur(@RequestBody Joueur joueur) {
        return joueurService.sauvegarderJoueur(joueur);
    }

    // PUT /api/joueurs/{id} : Mettre à jour un joueur
    @PutMapping("/{id}")
    public ResponseEntity<Joueur> updateJoueur(@PathVariable Long id, @RequestBody Joueur joueur) {
        // On s'assure que l'ID est bien celui de l'URL
        joueur.setId(id);
        return ResponseEntity.ok(joueurService.sauvegarderJoueur(joueur));
    }

    // DELETE /api/joueurs/{id} : Supprimer un joueur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoueur(@PathVariable Long id) {
        joueurService.supprimerJoueur(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/joueurs/{id}/jongles : Ajouter un score de jongle
    // On attend un objet JSON simple : { "date": "2023-10-27", "score": 50 }
    @PostMapping("/{id}/jongles")
    public Joueur addScoreJongle(@PathVariable Long id, @RequestBody ScoreJongle score) {
        return joueurService.ajouterScoreJongle(id, score.getDate(), score.getScore());
    }

    // POST /api/joueurs/{id}/presence : Basculer la présence pour une date
    // On attend la date en paramètre URL : /api/joueurs/1/presence?date=2023-10-27
    @PostMapping("/{id}/presence")
    public Joueur togglePresence(@PathVariable Long id, @RequestParam LocalDate date) {
        return joueurService.basculerPresence(id, date);
    }
}