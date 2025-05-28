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
    private static final String MENSAJE_1 = "La caminata con id = ";
    private static final String MENSAJE_2 = " no existe.";
    private static final String MENSAJE_3 = "El seguro con id = ";

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
            throw new EntityNotFoundException(MENSAJE_1 + caminataId + MENSAJE_2);
        }
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException(MENSAJE_3 + seguroId + MENSAJE_2);
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
            throw new EntityNotFoundException(MENSAJE_1 + caminataId + MENSAJE_2);
        }
        Optional<SeguroEntity> seguro = seguroRepository.findById(seguroId);
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException(MENSAJE_3 + seguroId + MENSAJE_2);
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
            throw new EntityNotFoundException(MENSAJE_1 + caminataId + MENSAJE_2);
        }
        Optional<SeguroEntity> seguro = seguroRepository.findById(seguroId);
        if(seguro.isEmpty())
        {
            throw new EntityNotFoundException(MENSAJE_3 + seguroId + MENSAJE_2);
        }
        seguroRepository.deleteById(seguroId);
        log.info("Termina proceso de eliminar un seguro de la caminata con id = {}", caminataId);  

    }
    
}
