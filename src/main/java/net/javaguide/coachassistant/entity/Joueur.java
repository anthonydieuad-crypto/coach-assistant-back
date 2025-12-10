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
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;
    private String photoUrl;
    
    private String nomParent;
    private String telParent;
    private String emailParent;

    @Column(name = "groupe_joueur") // "groupe" est un mot clé SQL, on le renomme en base
    private String groupe; 

    // Relation : Un joueur a plusieurs scores
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "joueur_id") 
    private List<ScoreJongle> historiqueJongles = new ArrayList<>();

    // Relation : Liste des dates de présence
    @ElementCollection
    @CollectionTable(name = "joueur_presences", joinColumns = @JoinColumn(name = "joueur_id"))
    @Column(name = "date_presence")
    private List<LocalDate> presences = new ArrayList<>();
}