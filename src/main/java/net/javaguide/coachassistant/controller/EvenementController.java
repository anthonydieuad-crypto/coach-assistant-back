package net.javaguide.coachassistant.controller;

import net.javaguide.coachassistant.dto.EvenementDto;
import net.javaguide.coachassistant.service.EvenementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@CrossOrigin("*")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    // GET /api/evenements?coachId=1
    @GetMapping
    public List<EvenementDto> getAllEvenements(@RequestParam(required = false) Long coachId) {
        return evenementService.recupererEvenementsDuCoach(coachId);
    }

    // POST /api/evenements?coachId=1
    @PostMapping
    public EvenementDto createEvenement(@RequestBody EvenementDto dto, @RequestParam(required = false) Long coachId) {
        return evenementService.sauvegarderEvenement(dto, coachId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementDto> updateEvenement(@PathVariable Long id, @RequestBody EvenementDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(evenementService.sauvegarderEvenement(dto, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return ResponseEntity.noContent().build();
    }
}