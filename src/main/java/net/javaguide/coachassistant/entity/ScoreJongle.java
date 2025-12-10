package net.javaguide.coachassistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreJongle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int score;

    // Constructeur pratique (sans ID)
    public ScoreJongle(LocalDate date, int score) {
        this.date = date;
        this.score = score;
    }
}