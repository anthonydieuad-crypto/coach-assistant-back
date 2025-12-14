package net.javaguide.coachassistant.repository;

import net.javaguide.coachassistant.entity.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    // 👇 Trouver uniquement les joueurs d'un coach spécifique
    List<Joueur> findByCoachId(Long coachId);
}