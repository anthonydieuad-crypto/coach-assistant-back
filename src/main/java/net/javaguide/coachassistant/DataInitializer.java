package net.javaguide.coachassistant;
import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UtilisateurRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Remplace par tes infos !
                Utilisateur admin = new Utilisateur("admin@club.com", "admin123", "ADMIN", "Klopp", "Jurgen");
                repository.save(admin);
                System.out.println("✅ Compte ADMIN créé : admin@club.com / admin123");
            }
        };
    }
}
