package co.edu.uniandes.dse.caminatas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import co.edu.uniandes.dse.caminatas.repositories.SeguroRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataSeguroService 
{
    @Autowired
    private CaminataRepository caminataRepository;

    @Autowired 
    private SeguroRepository seguroRepository;

    /*
     * Asocia un seguro existente a una caminata
     */
    @Transactional
    public SeguroEntity addSeguro(Long caminataId, Long seguroId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de agregar un seguro a la caminata con id = {}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        Optional<SeguroEntity> seguro = seguroRepository.findById(seguroId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException("El seguro con id = " + seguroId + " no existe.");
        }
        caminata.get().getSeguro();
        log.info("Termina proceso de agregar un seguro a la caminata con id = {}", caminataId);
        return seguro.get();
    }

    @Transactional
    public SeguroEntity getSeguro(Long caminataId, Long seguroId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de obtener un seguro de la caminata con id = {}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        Optional<SeguroEntity> seguro = seguroRepository.findById(seguroId);
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException("El seguro con id = " + seguroId + " no existe.");
        }
        log.info("Termina proceso de obtener un seguro de la caminata con id = {}", caminataId);
        return seguro.get();
    }

    /*
     * Desasocia un seguro existente de una caminata 
     */
    @Transactional
    public void removeSeguro(Long caminataId, Long seguroId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de eliminar un seguro de la caminata con id = {}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        Optional<SeguroEntity> seguro = seguroRepository.findById(seguroId);
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException("El seguro con id = " + seguroId + " no existe.");
        }
        log.info("Termina proceso de eliminar un seguro de la caminata con id = {}", caminataId);   
    }
    
}
