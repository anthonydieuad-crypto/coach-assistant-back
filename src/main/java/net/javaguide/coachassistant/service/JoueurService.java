package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.entity.ScoreJongle;
import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.JoueurRepository;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;
    private final UtilisateurRepository utilisateurRepository;

    public JoueurService(JoueurRepository joueurRepository, UtilisateurRepository utilisateurRepository) {
        this.joueurRepository = joueurRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Récupérer les joueurs d'un coach
    public List<Joueur> recupererJoueursDuCoach(Long coachId) {
        if (coachId == null) return joueurRepository.findAll(); // Sécurité ou Admin
        return joueurRepository.findByCoachId(coachId);
    }

    public Joueur recupererJoueurParId(Long id) {
        return joueurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé avec l'ID : " + id));
    }

    // Sauvegarder un joueur en l'associant à son coach
    public Joueur sauvegarderJoueur(Joueur joueur, Long coachId) {
        // Si c'est une création (id null) et qu'un coachId est fourni, on associe
        if (coachId != null && joueur.getCoach() == null) {
            Utilisateur coach = utilisateurRepository.findById(coachId)
                    .orElseThrow(() -> new RuntimeException("Coach introuvable"));
            joueur.setCoach(coach);
        }

        if (joueur.getPhotoUrl() == null || joueur.getPhotoUrl().isEmpty()) {
            joueur.setPhotoUrl("https://picsum.photos/seed/" + joueur.getPrenom() + "/200");
        }
        return joueurRepository.save(joueur);
    }

    public void supprimerJoueur(Long id) {
        joueurRepository.deleteById(id);
    }

    public Joueur ajouterScoreJongle(Long joueurId, LocalDate date, int score) {
        Joueur joueur = recupererJoueurParId(joueurId);
        joueur.getHistoriqueJongles().add(new ScoreJongle(date, score));
        return joueurRepository.save(joueur);
    }

    public Joueur basculerPresence(Long joueurId, LocalDate date) {
        Joueur joueur = recupererJoueurParId(joueurId);
        if (joueur.getPresences().contains(date)) {
            joueur.getPresences().remove(date);
        } else {
            joueur.getPresences().add(date);
        }
        return joueurRepository.save(joueur);
    }
}