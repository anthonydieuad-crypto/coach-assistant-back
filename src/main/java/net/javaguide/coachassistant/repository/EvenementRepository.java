package net.javaguide.coachassistant.repository;

import net.javaguide.coachassistant.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    // 👇 Trouver uniquement les événements d'un coach spécifique
    List<Evenement> findByCoachId(Long coachId);
}