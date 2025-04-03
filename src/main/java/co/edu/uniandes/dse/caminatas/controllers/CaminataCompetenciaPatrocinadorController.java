package co.edu.uniandes.dse.caminatas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.caminatas.dto.PatrocinadorDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.services.CaminataCompetenciaPatrocinadorService;

@RestController
@RequestMapping("/caminataCompetencias")
public class CaminataCompetenciaPatrocinadorController {

    @Autowired
    private CaminataCompetenciaPatrocinadorService caminataCompetenciaPatrocinadorService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Agregar un patrocinador a una caminata de competencia
     *
     * @param caminataCompetenciaId ID de la caminata de competencia
     * @param patrocinadorId ID del patrocinador a agregar
     * @return CaminataCompetenciaEntity con el patrocinador agregado
     * @throws EntityNotFoundException Si la caminata o el patrocinador no existen
     */
    @PostMapping("/{caminataCompetenciaId}/patrocinadores/{patrocinadorId}")
    public ResponseEntity<CaminataCompetenciaEntity> addPatrocinador(
            @PathVariable Long caminataCompetenciaId,
            @PathVariable Long patrocinadorId) throws EntityNotFoundException {
        CaminataCompetenciaEntity updatedCaminata = caminataCompetenciaPatrocinadorService.addPatrocinador(caminataCompetenciaId, patrocinadorId);
        return ResponseEntity.ok(updatedCaminata);
    }

    /**
     * Obtener el patrocinador de una caminata de competencia
     *
     * @param caminataCompetenciaId ID de la caminata de competencia
     * @return PatrocinadorEntity asociado a la caminata
     * @throws EntityNotFoundException Si la caminata o el patrocinador no existen
     */
    @GetMapping("/{caminataCompetenciaId}/patrocinadores/{patrocinadorId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PatrocinadorDetailDTO getPatrocinador(
            @PathVariable Long caminataCompetenciaId,
            @PathVariable Long patrocinadorId) throws EntityNotFoundException {
        
        PatrocinadorEntity patrocinador = caminataCompetenciaPatrocinadorService.getPatrocinador(caminataCompetenciaId);
        return modelMapper.map(patrocinador, PatrocinadorDetailDTO.class);
    }

    /**
     * Eliminar el patrocinador de una caminata de competencia
     *
     * @param caminataCompetenciaId ID de la caminata de competencia
     * @throws EntityNotFoundException Si la caminata no existe
     */
    @DeleteMapping("/{caminataCompetenciaId}/patrocinadores/{patrocinadorId}")
    public ResponseEntity<Void> removePatrocinador(@PathVariable Long caminataCompetenciaId, @PathVariable Long patrocinadorId) throws EntityNotFoundException {
        caminataCompetenciaPatrocinadorService.removePatrocinador(caminataCompetenciaId, patrocinadorId);
        return ResponseEntity.noContent().build();
    }
}