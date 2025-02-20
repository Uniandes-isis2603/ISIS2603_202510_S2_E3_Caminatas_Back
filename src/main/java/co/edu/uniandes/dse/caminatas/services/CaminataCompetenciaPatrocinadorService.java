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
     * Reemplaza el patrocinador de una caminata de competencia
     */
    public PatrocinadorEntity replacePatrocinador(Long caminataId, Long patrocinadorId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de reemplazar el patrocinador de la caminata con id = {0}", caminataId);
        Optional<CaminataCompetenciaEntity> caminataCompetencia = caminataCompetenciaRepository.findById(caminataId);
        Optional<PatrocinadorEntity> patrocinador = patrocinadorRepository.findById(patrocinadorId);
        if(caminataCompetencia.isEmpty())
        {
            throw new EntityNotFoundException("No se encontró la caminata con id = " + caminataId);
        }
        if(patrocinador.isEmpty())
        {
            throw new EntityNotFoundException("No se encontró el patrocinador con id = " + patrocinadorId);
        }
        caminataCompetencia.get().setPatrocinador(patrocinador.get());
        log.info("Termina proceso de reemplazar el patrocinador de la caminata con id = {0}", caminataId);
        return patrocinador.get();
    }

    /*
     * Borra el patrocinador de una caminata de competencia
     */
    @Transactional
    public void removePatrocinador(Long caminataId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de borrar el patrocinador de la caminata con id = {0}", caminataId);
        Optional<CaminataCompetenciaEntity> caminataCompetencia = caminataCompetenciaRepository.findById(caminataId);
        if(caminataCompetencia.isEmpty())
        {
            throw new EntityNotFoundException("No se encontró la caminata con id = " + caminataId);
        }
        Optional<PatrocinadorEntity> patrocinadorEntity = patrocinadorRepository.findById(caminataCompetencia.get().getPatrocinador().getId());
        patrocinadorEntity.ifPresent(patrocinador -> patrocinador.getCaminatasCompetencia().remove(caminataCompetencia.get()));
        caminataCompetencia.get().setPatrocinador(null);
        log.info("Termina proceso de borrar el patrocinador de la caminata con id = {0}", caminataId);
    } 
    
}
