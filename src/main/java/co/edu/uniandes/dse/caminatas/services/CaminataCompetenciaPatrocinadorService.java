package co.edu.uniandes.dse.caminatas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataCompetenciaRepository;
import co.edu.uniandes.dse.caminatas.repositories.PatrocinadorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataCompetenciaPatrocinadorService 
{
    @Autowired
    private CaminataCompetenciaRepository caminataCompetenciaRepository;

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    /*
     * Agrega un patrocinador a una caminata de competencia
     */
    @Transactional
    public CaminataCompetenciaEntity addPatrocinador(Long caminataCompetenciaId, Long patrocinadorId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de agregar el patrocinador a la caminata con id = {}", caminataCompetenciaId);
        CaminataCompetenciaEntity caminataCompetencia = caminataCompetenciaRepository.findById(caminataCompetenciaId)
            .orElseThrow(() -> new EntityNotFoundException("No se encontró la caminata de competencia con id = " + caminataCompetenciaId));

        PatrocinadorEntity patrocinador = patrocinadorRepository.findById(patrocinadorId)
            .orElseThrow(() -> new EntityNotFoundException("No se encontró el patrocinador con id = " + patrocinadorId));

        caminataCompetencia.setPatrocinador(patrocinador);
        log.info("Termina proceso de agregar el patrocinador a la caminata con id = {}", caminataCompetenciaId);
        return caminataCompetencia;
    }

    /*
     * Obtiene el patrocinador de una caminata de competencia
     */
    @Transactional
    public PatrocinadorEntity getPatrocinador(Long caminataCompetenciaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de consultar el patrocinador de la caminata con id = {}", caminataCompetenciaId);
        CaminataCompetenciaEntity caminataCompetencia = caminataCompetenciaRepository.findById(caminataCompetenciaId)
            .orElseThrow(() -> new EntityNotFoundException("No se encontró la caminata de competencia con id = " + caminataCompetenciaId));

        if (caminataCompetencia.getPatrocinador() == null) {
            throw new EntityNotFoundException("La caminata de competencia con id = " + caminataCompetenciaId + " no tiene un patrocinador asociado.");
        }

        log.info("Termina proceso de consultar el patrocinador de la caminata con id = {}", caminataCompetenciaId);
        return caminataCompetencia.getPatrocinador();
    }


    /*
     * Borra el patrocinador de una caminata de competencia
     */
    @Transactional
    public void removePatrocinador(Long caminataCompetenciaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de borrar el patrocinador de la caminata con id = {}", caminataCompetenciaId);
        Optional<CaminataCompetenciaEntity> caminataCompetencia = caminataCompetenciaRepository.findById(caminataCompetenciaId);
        if(caminataCompetencia.isEmpty())
        {
            throw new EntityNotFoundException("No se encontró la caminata de competencia con id = " + caminataCompetenciaId);
        }
        Optional<PatrocinadorEntity> patrocinadorEntity = patrocinadorRepository.findById(caminataCompetencia.get().getPatrocinador().getId());
        patrocinadorEntity.ifPresent(patrocinador -> patrocinador.getCaminatasCompetencia().remove(caminataCompetencia.get()));
        caminataCompetencia.get().setPatrocinador(null);
        log.info("Termina proceso de borrar el patrocinador de la caminata con id = {}", caminataCompetenciaId);
    } 
    
}
