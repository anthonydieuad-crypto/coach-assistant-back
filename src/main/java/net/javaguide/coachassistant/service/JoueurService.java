package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.entity.Evenement;
import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.entity.ScoreJongle;
import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.EvenementRepository;
import net.javaguide.coachassistant.repository.JoueurRepository;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EvenementRepository evenementRepository;

    public JoueurService(JoueurRepository joueurRepository,
                         UtilisateurRepository utilisateurRepository,
                         EvenementRepository evenementRepository) {
        this.joueurRepository = joueurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.evenementRepository = evenementRepository; // ✅ L'erreur disparaît ici
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
    public Joueur sauvegarderJoueur(Joueur joueurModifie, Long coachId) {
        // 1. Si c'est une MISE À JOUR (l'ID existe déjà)
        if (joueurModifie.getId() != null) {
            Joueur joueurExistant = joueurRepository.findById(joueurModifie.getId())
                    .orElseThrow(() -> new RuntimeException("Joueur introuvable"));

            // On met à jour les infos simples
            joueurExistant.setPrenom(joueurModifie.getPrenom());
            joueurExistant.setNom(joueurModifie.getNom());
            joueurExistant.setGroupe(joueurModifie.getGroupe()); // 👈 C'est ça que tu voulais changer
            joueurExistant.setPhotoUrl(joueurModifie.getPhotoUrl());
            joueurExistant.setNomParent(joueurModifie.getNomParent());
            joueurExistant.setTelParent(joueurModifie.getTelParent());
            joueurExistant.setEmailParent(joueurModifie.getEmailParent());

            // IMPORTANT : On NE TOUCHE PAS au coach existant (on le garde)
            // On ne touche pas non plus à l'historique ou aux présences ici

            return joueurRepository.save(joueurExistant);
        }

        // 2. Si c'est une CRÉATION (Nouveau joueur)
        else {
            if (coachId != null) {
                Utilisateur coach = utilisateurRepository.findById(coachId)
                        .orElseThrow(() -> new RuntimeException("Coach introuvable"));
                joueurModifie.setCoach(coach);
            }
            // Image par défaut si vide
            if (joueurModifie.getPhotoUrl() == null || joueurModifie.getPhotoUrl().isEmpty()) {
                joueurModifie.setPhotoUrl("https://picsum.photos/seed/" + joueurModifie.getPrenom() + "/200");
            }
            return joueurRepository.save(joueurModifie);
        }
    }

    @Transactional
    public void supprimerJoueur(Long id) {
        // 1. On récupère les événements où le joueur est inscrit
        List<Evenement> events = evenementRepository.findByParticipants_Id(id);

        // 2. On le retire de la liste des participants pour chaque événement
        for (Evenement ev : events) {
            ev.getParticipants().removeIf(p -> p.getId().equals(id));
            evenementRepository.save(ev); // On sauvegarde l'événement mis à jour
        }

        // 3. Maintenant on peut supprimer le joueur sans erreur SQL
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