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
public class CaminanteCaminataService {

    private static final String MENSAJE_1 = "El caminante con id = ";
    private static final String MENSAJE_2 = " no existe.";
    private static final String MENSAJE_3 = "La caminata con id = ";

    @Autowired
    private CaminanteRepository caminanteRepository;

    @Autowired
    private CaminataRepository caminataRepository;

    @Transactional
    public CaminanteEntity addCaminata(Long caminanteId, Long caminataId) throws EntityNotFoundException {
        log.info("Inicia proceso de agregar una caminata al caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminanteId + MENSAJE_2);
        }
        if (caminata.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminataId + MENSAJE_2);
        }
        
        caminante.get().getCaminatas().add(caminata.get());
        log.info("Termina proceso de agregar una caminata al caminante con id = {}", caminanteId);
        return caminante.get();
    }

    @Transactional
    public List<CaminataEntity> listCaminatas(Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de obtener las caminatas del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminanteId + MENSAJE_2);
        }
        log.info("Termina proceso de obtener las caminatas del caminante con id = {}", caminanteId);
        return caminante.get().getCaminatas();
    }

    @Transactional
    public CaminataEntity getCaminata(Long caminanteId, Long caminataId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de obtener una caminata del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminanteId + MENSAJE_2);
        }
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminataId + MENSAJE_2);
        }
        
        if (!caminante.get().getCaminatas().contains(caminata.get())) {
            throw new IllegalOperationException("La caminata con id = " + caminataId + " no está asociada al caminante con id = " + caminanteId);
        }
        
        log.info("Termina proceso de obtener una caminata del caminante con id = {}", caminanteId);
        return caminata.get();
    }

    @Transactional
    public List<CaminataEntity> replaceCaminatas(Long caminanteId, List<CaminataEntity> caminatas) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar las caminatas del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminanteId + MENSAJE_2);
        }
        
        caminante.get().getCaminatas().clear();
        for (CaminataEntity caminata : caminatas) {
            Optional<CaminataEntity> caminataEntity = caminataRepository.findById(caminata.getId());
            if (caminataEntity.isEmpty()) {
                throw new EntityNotFoundException(MENSAJE_3 + caminata.getId() + MENSAJE_2);
            }
            caminante.get().getCaminatas().add(caminataEntity.get());
        }
        
        log.info("Termina proceso de reemplazar las caminatas del caminante con id = {}", caminanteId);
        return caminante.get().getCaminatas();
    }

    @Transactional
    public void removeCaminata(Long caminanteId, Long caminataId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar una caminata del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminanteId + MENSAJE_2);
        }
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminataId + MENSAJE_2);
        }
        
        caminante.get().getCaminatas().remove(caminata.get());
        log.info("Termina proceso de eliminar una caminata del caminante con id = {}", caminanteId);
    }
}
