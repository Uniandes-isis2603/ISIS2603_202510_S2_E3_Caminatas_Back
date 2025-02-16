package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminanteService {

    @Autowired
    private CaminanteRepository caminanteRepository;

    /**
     * Crea un nuevo Caminante en la base de datos.
     *
     * Reglas de negocio:
     * - El nombre es obligatorio y solo debe contener caracteres alfabéticos y espacios.
     * - El documento es obligatorio, único, solo dígitos y mínimo 8 dígitos.
     * - El correo es obligatorio y debe tener un formato válido.
     * - El teléfono es obligatorio, solo dígitos y exactamente 10 dígitos.
     * - La dirección es obligatoria.
     *
     * @param caminante la entidad con los datos nuevos.
     * @return la entidad Caminante creada.
     * @throws IllegalOperationException si se incumple alguna regla de negocio.
     */
    @Transactional
    public CaminanteEntity createCaminante(CaminanteEntity caminante) throws IllegalOperationException {
        log.info("Inicia proceso de creación del caminante");
        validateCaminante(caminante, false);
        List<CaminanteEntity> caminanteEntityList = caminanteRepository.findByDocumento(caminante.getDocumento());
        Optional<CaminanteEntity> caminanteEntity;
        if (caminanteEntityList.isEmpty()) {
            caminanteEntity = Optional.empty();
        } else {
            caminanteEntity = Optional.of(caminanteEntityList.get(0));
        }        if (caminanteEntity.isPresent()) {
            throw new IllegalOperationException("El documento ya existe.");
        }        
        CaminanteEntity nuevoCaminante = caminanteRepository.save(caminante);
        log.info("Termina proceso de creación del caminante con id = {}", nuevoCaminante.getId());
        return nuevoCaminante;
    }

    /**
     * Obtiene la lista de todos los Caminantes.
     *
     * @return Lista de CaminanteEntity.
     */
    @Transactional
    public List<CaminanteEntity> getCaminantes() {
        log.info("Inicia proceso de consultar todos los caminantes");
        List<CaminanteEntity> caminantes = caminanteRepository.findAll();
        log.info("Termina proceso de consultar todos los caminantes");
        return caminantes;
    }

    /**
     * Obtiene los datos de un Caminante a partir de su ID.
     *
     * @param caminanteId Identificador de la instancia a consultar.
     * @return Instancia de CaminanteEntity con los datos consultados.
     * @throws EntityNotFoundException si no se encuentra el caminante.
     */
    @Transactional
    public CaminanteEntity getCaminante(Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        if (caminanteEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el caminante con el id = " + caminanteId);
        }
        log.info("Termina proceso de consultar el caminante con id = {}", caminanteId);
        return caminanteEntity.get();
    }

    /**
     * Actualiza la información de un Caminante.
     *
     * @param caminanteId Identificador de la instancia a actualizar.
     * @param caminante Entidad con los nuevos datos.
     * @return Instancia de CaminanteEntity actualizada.
     * @throws EntityNotFoundException si no se encuentra el caminante.
     * @throws IllegalOperationException si se incumple alguna regla de negocio.
     */
    @Transactional
    public CaminanteEntity updateCaminante(Long caminanteId, CaminanteEntity caminante) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        if (caminanteEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el caminante con el id = " + caminanteId);
        }
        
        // Se verifica que no exista otro caminante con el mismo documento
        CaminanteEntity nuevoCaminante = caminanteEntity.get();
        if (!nuevoCaminante.getDocumento().equals(caminante.getDocumento())) {
            List<CaminanteEntity> otroCaminanteList = caminanteRepository.findByDocumento(caminante.getDocumento());
            Optional<CaminanteEntity> otroCaminante;
            if (otroCaminanteList.isEmpty()) {
                otroCaminante = Optional.empty();
            } else {
                otroCaminante = Optional.of(otroCaminanteList.get(0));
            }            if (otroCaminante.isPresent()) {
                throw new IllegalOperationException("El documento ya existe.");
            }
        }

        validateCaminante(caminante, true);
        
        // Se actualizan los datos
        nuevoCaminante.setNombre(caminante.getNombre());
        nuevoCaminante.setDocumento(caminante.getDocumento());
        nuevoCaminante.setCorreo(caminante.getCorreo());
        nuevoCaminante.setTelefono(caminante.getTelefono());
        nuevoCaminante.setDireccion(caminante.getDireccion());
        
        CaminanteEntity caminanteUpdated = caminanteRepository.save(nuevoCaminante);
        log.info("Termina proceso de actualizar el caminante con id = {}", caminanteId);
        return caminanteUpdated;
    }

    /**
     * Elimina un Caminante a partir de su ID.
     *
     * @param caminanteId Identificador de la instancia a eliminar.
     * @throws EntityNotFoundException si no se encuentra el caminante.
     */
    @Transactional
    public void deleteCaminante(Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar el caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        if (caminanteEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el caminante con el id = " + caminanteId);
        }
        caminanteRepository.deleteById(caminanteId);
        log.info("Termina proceso de borrar el caminante con id = {}", caminanteId);
    }

    /**
     * Valida las reglas de negocio para la entidad Caminante.
     *
     * @param caminante La entidad a validar.
     * @param isUpdate Indica si es actualización (true) o creación (false).
     * @throws IllegalOperationException si alguna regla no se cumple.
     */

    private void validateCaminante(CaminanteEntity caminante, boolean isUpdate) throws IllegalOperationException {
        if (caminante.getNombre() == null || caminante.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre es obligatorio y no puede estar vacío.");
        }
        if (caminante.getDocumento() == null || caminante.getDocumento().trim().isEmpty()) {
            throw new IllegalOperationException("El documento es obligatorio y no puede estar vacío.");
        }
        if (caminante.getCorreo() == null || caminante.getCorreo().trim().isEmpty()) {
            throw new IllegalOperationException("El correo es obligatorio y no puede estar vacío.");
        }
        if (caminante.getTelefono() == null || caminante.getTelefono().trim().isEmpty()) {
            throw new IllegalOperationException("El teléfono es obligatorio y no puede estar vacío.");
        }
        if (caminante.getDireccion() == null || caminante.getDireccion().trim().isEmpty()) {
            throw new IllegalOperationException("La dirección es obligatoria y no puede estar vacía.");
        }
        
        if (!caminante.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalOperationException("El nombre solo debe contener caracteres alfabéticos y espacios.");
        }
        
        if (!caminante.getDocumento().matches("^\\d{8,}$")) {
            throw new IllegalOperationException("El documento debe contener solo números y tener al menos 8 dígitos.");
        }
        
        if (!caminante.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalOperationException("El correo tiene un formato inválido.");
        }
        
        if (!caminante.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalOperationException("El teléfono debe contener solo números y tener una longitud de 10 dígitos.");
        }
    }
    
    
}
