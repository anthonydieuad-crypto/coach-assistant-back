package net.javaguide.coachassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "groupe_joueur")
    private String groupe;

    // 👇 Relation : Le joueur appartient à UN coach
    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonIgnore // Important : évite de renvoyer tout l'objet coach en JSON
    private Utilisateur coach;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "joueur_id")
    private List<ScoreJongle> historiqueJongles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "joueur_presences", joinColumns = @JoinColumn(name = "joueur_id"))
    @Column(name = "date_presence")
    private List<LocalDate> presences = new ArrayList<>();
}