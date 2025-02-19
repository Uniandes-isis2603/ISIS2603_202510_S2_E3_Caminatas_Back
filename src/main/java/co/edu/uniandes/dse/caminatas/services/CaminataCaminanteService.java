package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataCaminanteService {

    @Autowired
    private CaminataRepository caminataRepository;

    @Autowired 
    private CaminanteRepository caminanteRepository;

    /*
     * Asocia un caminante existente a una caminata
     */
    @Transactional
    public CaminataEntity addCaminante(Long caminataId, Long caminanteId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de agregar un caminante a la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        if(caminante.isEmpty())
        {
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");
        }
        caminata.get().getCaminantes().add(caminante.get());
        log.info("Termina proceso de agregar un caminante a la caminata con id = {0}", caminataId);
        return caminata.get();
    }

    /*
     * Obtiene una colección de instancias de CaminanteEntity asociadas a una instancia de Caminata
     */
    @Transactional  
    public List<CaminanteEntity> listCaminantes(Long caminataId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de obtener los caminantes de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        log.info("Termina proceso de obtener los caminantes de la caminata con id = {0}", caminataId);
        return caminata.get().getCaminantes();
    }

    /*
     * Obtiene una instancia de Caminante asociada a una instancia de Caminata
     */
    @Transactional
    public CaminanteEntity getCaminante(Long caminataId, Long caminanteId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de obtener un caminante de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if(caminante.isEmpty())
        {
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");
        }
        log.info("Termina proceso de obtener un caminante de la caminata con id = {0}", caminataId);
        if(!caminata.get().getCaminantes().contains(caminante.get()))
        {
            throw new IllegalOperationException("El caminante con id = " + caminanteId + " no está asociado a la caminata con id = " + caminataId);
        }
        return caminante.get();
    }

    /*
     * Remplaza la colección de instancias de CaminanteEntity asociadas a una instancia de Caminata
     */
    @Transactional
    public List<CaminanteEntity> replaceCaminantes(Long caminataId, List<CaminanteEntity> caminantes) throws EntityNotFoundException
    {
        log.info("Inicia proceso de remplazar los caminantes de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        for(CaminanteEntity caminante: caminantes)
        {
            Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminante.getId());
            if(caminanteEntity.isEmpty())
            {
                throw new EntityNotFoundException("El caminante con id = " + caminante.getId() + " no existe.");
            }
            if(!caminata.get().getCaminantes().contains(caminanteEntity.get()))
            {
                caminata.get().getCaminantes().add(caminanteEntity.get());
            }
    
        }
        log.info("Termina proceso de remplazar los caminantes de la caminata con id = {0}", caminataId);
        return caminata.get().getCaminantes();
    }

    /*
     * Desasocia un caminante existente de una caminata 
     */
    @Transactional
    public void removeCaminante(Long caminataId, Long caminanteId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de eliminar un caminante de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if(caminante.isEmpty())
        {
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");
        }
        caminata.get().getCaminantes().remove(caminante.get());
        log.info("Termina proceso de eliminar un caminante de la caminata con id = {0}", caminataId);   
    }
}
