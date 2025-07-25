package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import co.edu.uniandes.dse.caminatas.repositories.SeguroRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeguroService { 

    private static final String MENSAJE_1 = "No se encontró el seguro con el id = ";

    @Autowired
    SeguroRepository seguroRepository;

    @Autowired
    CaminataRepository caminataRepository;

    @Transactional
    public SeguroEntity createSeguro(SeguroEntity entity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia el proceso de creación de un seguro");
        
        if(entity.getCaminata() == null)
            throw new IllegalOperationException("Caminata no puede ser nulo");

        Optional<CaminataEntity> caminataEntity = caminataRepository.findById(entity.getCaminata().getId());
        if (caminataEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la caminata con el id = " + entity.getCaminata().getId());
        }

        if(caminataEntity.get().getSeguro() != null) {
            throw new IllegalOperationException("La caminata ya tiene un seguro asociado");
        }

        if(!seguroRepository.findByNumero(entity.getNumero()).isEmpty()) {
            throw new IllegalOperationException("Ya existe un seguro con el número = " + entity.getNumero());
        }

        if(entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre del seguro no puede ser nulo o vacío.");
        }

        if (entity.getNumero() == null) {
            throw new IllegalOperationException("El número del seguro no puede ser nulo o vacío.");
        }

        if(entity.getTipo() == null || entity.getTipo().isEmpty()) {
            throw new IllegalOperationException("El tipo del seguro no puede ser nulo o vacío.");
        }

        if(entity.getCosto() == 0) {
            throw new IllegalOperationException("El costo del seguro no puede ser nulo o vacío.");
        }

        SeguroEntity savedEntity = seguroRepository.save(entity);
        log.info("Finaliza el proceso de creación de un seguro");
        return savedEntity;
    }

    @Transactional
    public SeguroEntity updateSeguro(Long seguroId, SeguroEntity entity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia el proceso de actualización de un seguro con id = {0}", seguroId);
        Optional<SeguroEntity> seguroEntity = seguroRepository.findById(seguroId);
        if (seguroEntity.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + seguroId);
        }

        if(entity.getNombre() == null || entity.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre del seguro no puede ser nulo o vacío.");
        }

        if(entity.getTipo() == null || entity.getTipo().isEmpty()) {
            throw new IllegalOperationException("El tipo del seguro no puede ser nulo o vacío.");
        }

        entity.setId(seguroId);
        SeguroEntity updatedEntity = seguroRepository.save(entity);
        log.info("Finaliza el proceso de actualización de un seguro con id = {0}", seguroId);
        return updatedEntity;
    }

    @Transactional
    public void deleteSeguro(Long id) throws EntityNotFoundException {
        log.info("Inicia el proceso de eliminación de un seguro con id = {0}", id);
        Optional<SeguroEntity> seguroEntity = seguroRepository.findById(id);
        if (seguroEntity.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + id);
        }
        seguroRepository.deleteById(id);
        log.info("Finaliza el proceso de eliminación de un seguro con id = {0}", id);
    }

    @Transactional
    public SeguroEntity getSeguro(Long seguroId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el seguro con id = {0}", seguroId);
        Optional<SeguroEntity> seguroEntity = seguroRepository.findById(seguroId);
        if (seguroEntity.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + seguroId);
        }
        log.info("Termina proceso de consultar el seguro con id = {0}", seguroId);
        return seguroEntity.get();
    }

    @Transactional
    public List<SeguroEntity> getSeguros() {
        log.info("Inicia el proceso de consultar los seguros");
        return seguroRepository.findAll();
    }
}
