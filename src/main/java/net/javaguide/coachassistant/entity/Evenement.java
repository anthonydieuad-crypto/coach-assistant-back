package net.javaguide.coachassistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String titre;
    private String type; // 'training', 'plateau', 'match'...
    private String lieu;
    
    @Column(name = "equipes_adverses")
    private String equipesAdverses;
    
    @Column(name = "groupe_evenement")
    private String groupe;

    // Relation : Un événement a plusieurs participants (Joueurs)
    @ManyToMany
    @JoinTable(
        name = "evenement_participants",
        joinColumns = @JoinColumn(name = "evenement_id"),
        inverseJoinColumns = @JoinColumn(name = "joueur_id")
    )
    private List<Joueur> participants = new ArrayList<>();
}