package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.dto.EvenementDto;
import net.javaguide.coachassistant.service.EvenementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@CrossOrigin("*") // Autorise Angular à nous parler
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    // GET /api/evenements
    @GetMapping
    public List<EvenementDto> getAllEvenements() {
        return evenementService.recupererTousLesEvenements();
    }

    // POST /api/evenements (Création)
    @PostMapping
    public EvenementDto createEvenement(@RequestBody EvenementDto dto) {
        return evenementService.sauvegarderEvenement(dto);
    }

    // PUT /api/evenements/{id} (Mise à jour)
    @PutMapping("/{id}")
    public ResponseEntity<EvenementDto> updateEvenement(@PathVariable Long id, @RequestBody EvenementDto dto) {
        dto.setId(id); // Sécurité : on force l'ID de l'URL
        return ResponseEntity.ok(evenementService.sauvegarderEvenement(dto));
    }

    // DELETE /api/evenements/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return ResponseEntity.noContent().build();
    }
}