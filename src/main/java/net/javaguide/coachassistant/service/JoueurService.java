package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.entity.ScoreJongle;
import net.javaguide.coachassistant.repository.JoueurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;

    public JoueurService(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    public List<Joueur> recupererTousLesJoueurs() {
        return joueurRepository.findAll();
    }

    public Joueur recupererJoueurParId(Long id) {
        return joueurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé avec l'ID : " + id));
    }

    public Joueur sauvegarderJoueur(Joueur joueur) {
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
        
        // On utilise l'entité ScoreJongle
        ScoreJongle nouveauScore = new ScoreJongle(date, score);
        
        joueur.getHistoriqueJongles().add(nouveauScore);
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