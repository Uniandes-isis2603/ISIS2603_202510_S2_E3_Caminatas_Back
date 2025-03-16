package co.edu.uniandes.dse.caminatas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.services.CaminataCompetenciaPatrocinadorService;

@RestController
@RequestMapping("/caminatasCompetencia")
public class CaminataCompetenciaPatrocinadorController {

    @Autowired
    private CaminataCompetenciaPatrocinadorService caminataCompetenciaPatrocinadorService;

    /**
     * Reemplazar el patrocinador de una caminata de competencia
     *
     * @param caminataCompetenciaId ID de la caminata de competencia
     * @param patrocinadorId ID del nuevo patrocinador
     * @return CaminataCompetenciaEntity con el patrocinador actualizado
     * @throws EntityNotFoundException Si la caminata o el patrocinador no existen
     */
    @PutMapping("/{caminataCompetenciaId}/patrocinador/{patrocinadorId}")
    public ResponseEntity<CaminataCompetenciaEntity> replacePatrocinador(
            @PathVariable Long caminataCompetenciaId,
            @PathVariable Long patrocinadorId) throws EntityNotFoundException {
        CaminataCompetenciaEntity updatedCaminata = caminataCompetenciaPatrocinadorService.replacePatrocinador(caminataCompetenciaId, patrocinadorId);
        return ResponseEntity.ok(updatedCaminata);
    }

    /**
     * Eliminar el patrocinador de una caminata de competencia
     *
     * @param caminataCompetenciaId ID de la caminata de competencia
     * @throws EntityNotFoundException Si la caminata no existe
     */
    @DeleteMapping("/{caminataCompetenciaId}/patrocinador")
    public ResponseEntity<Void> removePatrocinador(@PathVariable Long caminataCompetenciaId) throws EntityNotFoundException {
        caminataCompetenciaPatrocinadorService.removePatrocinador(caminataCompetenciaId);
        return ResponseEntity.noContent().build();
    }
}
