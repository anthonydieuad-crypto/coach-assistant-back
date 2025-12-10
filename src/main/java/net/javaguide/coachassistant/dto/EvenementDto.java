package net.javaguide.coachassistant.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EvenementDto {
    private Long id;
    private LocalDate date;
    private String titre;
    private String type;
    private String lieu;
    private String equipesAdverses;
    private String groupe;
    
    // Le front envoie une liste d'IDs, on les stocke ici avant conversion
    private List<Long> participants; 
}