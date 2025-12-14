package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.entity.Utilisateur;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerAlerteNouveauCoach(Utilisateur nouveauCoach) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("anthonydieuad@gmail.com");
        message.setTo("anthonydieuad@gmail.com");
        message.setSubject("🚀 Nouveau Coach inscrit !");
        message.setText("Salut Anthony,\n\n" +
                "Un nouvel entraîneur vient de créer son compte sur Coach Assistant :\n\n" +
                "Nom : " + nouveauCoach.getPrenom() + " " + nouveauCoach.getNom() + "\n" +
                "Email : " + nouveauCoach.getEmail() + "\n\n" +
                "Bienvenue à lui ! ⚽");

        mailSender.send(message);
        System.out.println("📧 Mail d'alerte envoyé pour " + nouveauCoach.getEmail());
    }
}