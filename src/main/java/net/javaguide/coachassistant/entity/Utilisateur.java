package net.javaguide.coachassistant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utilisateurs")
@Getter // 👈 Génère getId(), getEmail(), etc. pour TOUS les champs
@Setter // 👈 Génère setId(), setEmail(), etc. (utile pour JPA)
@NoArgsConstructor // 👈 Génère le constructeur vide public Utilisateur() {}
@AllArgsConstructor // 👈 Génère le constructeur avec tous les arguments
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String role;
    private String nom;
    private String prenom;

    // Tu n'as plus besoin d'écrire les constructeurs manuellement grâce aux annotations Lombok ci-dessus !
    // Mais si tu veux garder ton constructeur spécifique (sans ID), tu peux le laisser ici :

    public Utilisateur(String email, String password, String role, String nom, String prenom) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
    }
}