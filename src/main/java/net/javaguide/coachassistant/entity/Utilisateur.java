package net.javaguide.coachassistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String role;
    private String nom;
    private String prenom;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Joueur>joueurs;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Evenement> evenements;


    public Utilisateur(String email, String password, String role, String nom, String prenom) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
    }
}