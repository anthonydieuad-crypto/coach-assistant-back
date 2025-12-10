package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.dto.EvenementDto;
import net.javaguide.coachassistant.entity.Evenement;
import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.repository.EvenementRepository;
import net.javaguide.coachassistant.repository.JoueurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final JoueurRepository joueurRepository;

    public EvenementService(EvenementRepository evenementRepository, JoueurRepository joueurRepository) {
        this.evenementRepository = evenementRepository;
        this.joueurRepository = joueurRepository;
    }

    public List<EvenementDto> recupererTousLesEvenements() {
        return evenementRepository.findAll().stream()
                .map(this::convertirEnDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EvenementDto sauvegarderEvenement(EvenementDto dto) {
        Evenement evenement = new Evenement();
        
        if (dto.getId() != null) {
            evenement = evenementRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        }

        // Mapping simple
        evenement.setDate(dto.getDate());
        evenement.setTitre(dto.getTitre());
        evenement.setType(dto.getType());
        evenement.setLieu(dto.getLieu());
        evenement.setEquipesAdverses(dto.getEquipesAdverses());
        evenement.setGroupe(dto.getGroupe());

        // Mapping complexe : IDs -> Objets Joueurs
        if (dto.getParticipants() != null) {
            List<Joueur> participants = joueurRepository.findAllById(dto.getParticipants());
            evenement.setParticipants(participants);
        }

        Evenement evenementSauvegarde = evenementRepository.save(evenement);
        return convertirEnDto(evenementSauvegarde);
    }

    public void supprimerEvenement(Long id) {
        evenementRepository.deleteById(id);
    }

   // Convertisseur Entité -> DTO
    private EvenementDto convertirEnDto(Evenement entity) {
        EvenementDto dto = new EvenementDto();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        
        dto.setTitre(entity.getTitre()); 
        
        dto.setType(entity.getType());
        dto.setLieu(entity.getLieu());
        dto.setEquipesAdverses(entity.getEquipesAdverses());
        dto.setGroupe(entity.getGroupe());
        
        List<Long> idsParticipants = entity.getParticipants().stream()
                .map(Joueur::getId)
                .collect(Collectors.toList());
        dto.setParticipants(idsParticipants);
        
        return dto;
    }
}