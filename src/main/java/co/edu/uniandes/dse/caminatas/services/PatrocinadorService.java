package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.PatrocinadorRepository;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class PatrocinadorService {

    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @Transactional
    public PatrocinadorEntity createPatrocinador(PatrocinadorEntity patrocinador) throws IllegalOperationException {
        log.info("Inicia proceso de creación del patrocinador");
        validatePatrocinador(patrocinador);
        List<PatrocinadorEntity> patrocinadorEntityList = patrocinadorRepository.findByDocumento(patrocinador.getDocumento());
        Optional<PatrocinadorEntity> patrocinadorEntity;
        if (patrocinadorEntityList.isEmpty()) {
            patrocinadorEntity = Optional.empty();
        } else {
            patrocinadorEntity = Optional.of(patrocinadorEntityList.get(0));
        }        
        /**if (patrocinadorEntity.isPresent()) {
            throw new IllegalOperationException("El documento ya existe.");
        }   */
        PatrocinadorEntity nuevoPatrocinador = patrocinadorRepository.save(patrocinador);
        log.info("Termina proceso de creación del patrocinador con id = {}", nuevoPatrocinador.getId());
        return nuevoPatrocinador;
    }

    @Transactional
    public List<PatrocinadorEntity> getPatrocinadores() {
        log.info("Inicia proceso de consultar todos los patrocinadores");
        List<PatrocinadorEntity> patrocinadores = patrocinadorRepository.findAll();
        log.info("Termina proceso de consultar todos los patrocinadores");
        return patrocinadores;
    }

    @Transactional
    public PatrocinadorEntity getPatrocinador(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el patrocinador con id = {}", id);
        Optional<PatrocinadorEntity> patrocinadorEntity = patrocinadorRepository.findById(id);
        if (patrocinadorEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el patrocinador con el id = " + id);
        }
        log.info("Termina proceso de consultar el patrocinador con id = {}", id);
        return patrocinadorEntity.get();
    }

    @Transactional
    public PatrocinadorEntity updatePatrocinador(Long id, PatrocinadorEntity patrocinador) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el patrocinador con id = {}", id);
        Optional<PatrocinadorEntity> patrocinadorEntity = patrocinadorRepository.findById(id);
        if (patrocinadorEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el patrocinador con el id = " + id);
        }
        validatePatrocinador(patrocinador);
        PatrocinadorEntity patrocinadorActualizado = patrocinadorEntity.get();
        patrocinadorActualizado.setNombre(patrocinador.getNombre());
        patrocinadorActualizado.setDocumento(patrocinador.getDocumento());
        patrocinadorActualizado.setCorreo(patrocinador.getCorreo());
        patrocinadorActualizado.setTelefono(patrocinador.getTelefono());
        PatrocinadorEntity actualizado = patrocinadorRepository.save(patrocinadorActualizado);
        log.info("Termina proceso de actualizar el patrocinador con id = {}", id);
        return actualizado;
    }

    @Transactional
    public void deletePatrocinador(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar el patrocinador con id = {}", id);
        Optional<PatrocinadorEntity> patrocinadorEntity = patrocinadorRepository.findById(id);
        if (patrocinadorEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el patrocinador con el id = " + id);
        }
        patrocinadorRepository.deleteById(id);
        log.info("Termina proceso de borrar el patrocinador con id = {}", id);
    }

    private void validatePatrocinador(PatrocinadorEntity patrocinador) throws IllegalOperationException {
        if (patrocinador.getNombre() == null || !patrocinador.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalOperationException("El nombre solo debe contener caracteres alfabéticos y espacios.");
        }
        if (patrocinador.getDocumento() == null || !patrocinador.getDocumento().matches("^\\d{8,}$")) {
            throw new IllegalOperationException("El documento debe contener solo números y tener al menos 8 dígitos.");
        }
        if (patrocinador.getCorreo() == null || !patrocinador.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalOperationException("El correo tiene un formato inválido.");
        }
        if (patrocinador.getTelefono() == null || !patrocinador.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalOperationException("El teléfono debe contener solo números y tener una longitud de 10 dígitos.");
        }
    }
    
}