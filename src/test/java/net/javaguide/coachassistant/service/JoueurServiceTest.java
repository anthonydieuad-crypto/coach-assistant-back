package net.javaguide.coachassistant.service;

import net.javaguide.coachassistant.entity.Evenement;
import net.javaguide.coachassistant.entity.Joueur;
import net.javaguide.coachassistant.entity.Utilisateur;
import net.javaguide.coachassistant.repository.EvenementRepository;
import net.javaguide.coachassistant.repository.JoueurRepository;
import net.javaguide.coachassistant.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JoueurServiceTest {

    @Mock
    private JoueurRepository joueurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private EvenementRepository evenementRepository;

    @InjectMocks
    private JoueurService joueurService;

    private Joueur joueurTest;

    @BeforeEach
    void setUp() {
        joueurTest = new Joueur();
        joueurTest.setId(1L);
        joueurTest.setPrenom("Zinedine");
        joueurTest.setNom("Zidane");
    }

    @Test
    void recupererJoueurParId_DoitRetournerLeJoueur() {
        //ARRANGE
        when(joueurRepository.findById(1L)).thenReturn(Optional.of(joueurTest));

        //ACT
        Joueur resultat = joueurService.recupererJoueurParId(1L);

        //ASSERT
        assertNotNull(resultat, "Le joueur ne doit pas être null");
        assertEquals("Zinedine", resultat.getPrenom(), "Le prénom doit être Zinedine");

        verify(joueurRepository, times(1)).findById(1L);
    }

    @Test
    void recupererJoueurParId_DoitLeverUneErreur_QuandJoueurNExistePas() {
        //ARRANGE
        when(joueurRepository.findById(999L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> joueurService.recupererJoueurParId(999L));

        //ASSERT
        assertEquals("Joueur non trouvé avec l'ID : 999", exception.getMessage());
        verify(joueurRepository, times(1)).findById(999L);
    }

    @Test
    void basculerPresence_DoitAjouterLaDate_QuandElleNestPasEncoreLa() {
        //ARRANGE
        LocalDate dateAujourdhui = LocalDate.now();
        when(joueurRepository.findById(1L)).thenReturn(Optional.of(joueurTest));
        when(joueurRepository.save(any(Joueur.class))).thenReturn(joueurTest);

        //ACT
        Joueur resultat = joueurService.basculerPresence(1L, dateAujourdhui);

        //ASSERT
        assertTrue(resultat.getPresences().contains(dateAujourdhui), "La date devrais être ajoutée aux présences");
        verify(joueurRepository, times(1)).save(joueurTest);
    }

    @Test
    void basculerPresence_DoitRetirerLaDate_QuandElleYEstDeja() {
        //ARRANGE
        LocalDate dateAujourdhui = LocalDate.now();
        joueurTest.getPresences().add(dateAujourdhui);

        when(joueurRepository.findById(1L)).thenReturn(Optional.of(joueurTest));
        when(joueurRepository.save(any(Joueur.class))).thenReturn(joueurTest);

        //ACT
        Joueur resultat = joueurService.basculerPresence(1L, dateAujourdhui);

        //ASSERT
        assertFalse(resultat.getPresences().contains(dateAujourdhui), "La date devrait être retirée des présences");
        verify(joueurRepository, times(1)).save(joueurTest);
    }

    @Test
    void supprimerJoueur_DoitRetirerLeJoueurDesEvenements_PuisLeSupprimer() {
        //ARRANGE
        Evenement fauxEvenement = new Evenement();
        fauxEvenement.setId(10L);
        fauxEvenement.getParticipants().add(joueurTest);

        when(evenementRepository.findByParticipants_Id(1L)).thenReturn(List.of(fauxEvenement));

        //ACT
        joueurService.supprimerJoueur(1L);

        //ASSERT
        assertFalse(fauxEvenement.getParticipants().contains(joueurTest), "Le joueur doit être retiré de l'événement");

        verify(evenementRepository, times(1)).save(fauxEvenement);
        verify(joueurRepository, times(1)).deleteById(1L);
    }

    @Test
    void sauvegarderJoueur_DoitMettreAJour_QuandLeJoueurAUnId() {
        //ARRANGE
        Joueur joueurModifie = new Joueur();
        joueurModifie.setId(1L);
        joueurModifie.setPrenom("Zinedine");
        joueurModifie.setNom("Zidane (Modifié)");
        joueurModifie.setGroupe("U15");

        when(joueurRepository.findById(1L)).thenReturn(Optional.of(joueurTest));
        when(joueurRepository.save(any(Joueur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //ACT
        Joueur resultat = joueurService.sauvegarderJoueur(joueurModifie, null);

        //ASSERT
        assertEquals("Zidane (Modifié)", resultat.getNom(), "Le nom doit avoir été mis à jour");
        assertEquals("U15", resultat.getGroupe(), "Le groupe doit avoir été mis à jour");

        verify(joueurRepository, times(1)).findById(1L);
        verify(joueurRepository, times(1)).save(any(Joueur.class));

    }

    @Test
    void sauvegarderJoueur_DoitCreerEtAssocierAuCoach_QuandNouveauJoueur() {
        //ARRANGE
        Joueur nouveauJoueur = new Joueur();
        nouveauJoueur.setPrenom("Thierry");
        nouveauJoueur.setNom("Henry");

        Utilisateur fauxCoach = new Utilisateur();
        fauxCoach.setId(99L);

        when(utilisateurRepository.findById(99L)).thenReturn(Optional.of(fauxCoach));
        when(joueurRepository.save(any(Joueur.class))).thenAnswer(i -> i.getArgument(0));

        //ACT
        Joueur resultat = joueurService.sauvegarderJoueur(nouveauJoueur, 99L);

        //ASSERT
        assertEquals(fauxCoach, resultat.getCoach(), "Le joueur doit être assigné au coach");
        assertNotNull(resultat.getPhotoUrl(), "Une URL de photo par défaut doit avoir été générée");
        assertTrue(resultat.getPhotoUrl().contains("Thierry"), "L'URL de la photo doit contenir le prénom");

        verify(utilisateurRepository, times(1)).findById(99L);
        verify(joueurRepository, times(1)).save(nouveauJoueur);
    }
}
