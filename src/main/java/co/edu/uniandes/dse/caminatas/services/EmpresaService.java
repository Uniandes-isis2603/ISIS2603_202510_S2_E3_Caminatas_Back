package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * Crea una nueva Empresa en la base de datos.
     *
     * Reglas de negocio:
     * - El nombre es obligatorio y solo debe contener caracteres alfabéticos y espacios.
     * - El documento es obligatorio, único, solo dígitos y mínimo 8 dígitos.
     * - El correo es obligatorio y debe tener un formato válido.
     *
     * @param empresa la entidad con los datos nuevos.
     * @return la entidad Empresa creada.
     * @throws IllegalOperationException si se incumple alguna regla de negocio.
     */
    @Transactional
    public EmpresaEntity createEmpresa(EmpresaEntity empresa) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la empresa");
        validateEmpresa(empresa, false);
        
        List<EmpresaEntity> empresaEntityList = empresaRepository.findByDocumento(empresa.getDocumento());
        Optional<EmpresaEntity> empresaEntity;
        if (empresaEntityList.isEmpty()) {
            empresaEntity = Optional.empty();
        } else {
            empresaEntity = Optional.of(empresaEntityList.get(0));
        }
        
        if (empresaEntity.isPresent()) {
            throw new IllegalOperationException("El documento ya existe.");
        }
        
        EmpresaEntity nuevaEmpresa = empresaRepository.save(empresa);
        log.info("Termina proceso de creación de la empresa con id = {}", nuevaEmpresa.getId());
        return nuevaEmpresa;
    }

    /**
     * Obtiene la lista de todas las Empresas.
     *
     * @return Lista de EmpresaEntity.
     */
    @Transactional
    public List<EmpresaEntity> getEmpresas() {
        log.info("Inicia proceso de consultar todas las empresas");
        List<EmpresaEntity> empresas = empresaRepository.findAll();
        log.info("Termina proceso de consultar todas las empresas");
        return empresas;
    }

    /**
     * Obtiene los datos de una Empresa a partir de su ID.
     *
     * @param empresaId Identificador de la instancia a consultar.
     * @return Instancia de EmpresaEntity con los datos consultados.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     */
    @Transactional
    public EmpresaEntity getEmpresa(Long empresaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con el id = " + empresaId);
        }
        log.info("Termina proceso de consultar la empresa con id = {}", empresaId);
        return empresaEntity.get();
    }

    /**
     * Actualiza la información de una Empresa.
     *
     * @param empresaId Identificador de la instancia a actualizar.
     * @param empresa Entidad con los nuevos datos.
     * @return Instancia de EmpresaEntity actualizada.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     * @throws IllegalOperationException si se incumple alguna regla de negocio.
     */
    @Transactional
    public EmpresaEntity updateEmpresa(Long empresaId, EmpresaEntity empresa) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con el id = " + empresaId);
        }
        
        // Se verifica que no exista otra empresa con el mismo documento
        EmpresaEntity nuevaEmpresa = empresaEntity.get();
        if (!nuevaEmpresa.getDocumento().equals(empresa.getDocumento())) {
            List<EmpresaEntity> otraEmpresaList = empresaRepository.findByDocumento(empresa.getDocumento());
            Optional<EmpresaEntity> otraEmpresa;
            if (otraEmpresaList.isEmpty()) {
                otraEmpresa = Optional.empty();
            } else {
                otraEmpresa = Optional.of(otraEmpresaList.get(0));
            }
            if (otraEmpresa.isPresent()) {
                throw new IllegalOperationException("El documento ya existe.");
            }
        }

        validateEmpresa(empresa, true);
        
        // Se actualizan los datos
        nuevaEmpresa.setNombre(empresa.getNombre());
        nuevaEmpresa.setDocumento(empresa.getDocumento());
        nuevaEmpresa.setCorreo(empresa.getCorreo());
        
        EmpresaEntity empresaUpdated = empresaRepository.save(nuevaEmpresa);
        log.info("Termina proceso de actualizar la empresa con id = {}", empresaId);
        return empresaUpdated;
    }

    /**
     * Elimina una Empresa a partir de su ID.
     *
     * @param empresaId Identificador de la instancia a eliminar.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     */
    @Transactional
    public void deleteEmpresa(Long empresaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con el id = " + empresaId);
        }
        empresaRepository.deleteById(empresaId);
        log.info("Termina proceso de borrar la empresa con id = {}", empresaId);
    }

    /**
     * Valida las reglas de negocio para la entidad Empresa.
     *
     * @param empresa La entidad a validar.
     * @param isUpdate Indica si es actualización (true) o creación (false).
     * @throws IllegalOperationException si alguna regla no se cumple.
     */
    private void validateEmpresa(EmpresaEntity empresa, boolean isUpdate) throws IllegalOperationException {
        if (empresa.getNombre() == null || empresa.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre es obligatorio y no puede estar vacío.");
        }
        if (empresa.getDocumento() == null || empresa.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El documento es obligatorio y no puede estar vacío.");
        }
        if (empresa.getCorreo() == null || empresa.getCorreo().trim().isEmpty()) {
            throw new IllegalOperationException("El correo es obligatorio y no puede estar vacío.");
        }
        
        if (!empresa.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalOperationException("El nombre solo debe contener caracteres alfabéticos y espacios.");
        }
        
        String documento = empresa.getDocumento().trim();
        if (!documento.matches("^\\d{9}-\\d$")) {
            throw new IllegalOperationException("El NIT debe tener el formato válido: 9 dígitos, un guion y 1 dígito de verificación (XXXXXXXXX-Y).");
        }

        // Separar el NIT y el dígito de verificación
        String[] partes = documento.split("-");
        String numeroDocumento = partes[0];

        // Verificar que el número del NIT no sea todo ceros
        if (numeroDocumento.matches("^0+$")) {
            throw new IllegalOperationException("El número del NIT no puede ser todo ceros.");
        }
        
        if (!empresa.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalOperationException("El correo tiene un formato inválido.");
        }
    }
}