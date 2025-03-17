package co.edu.uniandes.dse.caminatas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.EmpresaRepository;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpresaPagoService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    /**
     * Asocia un pago existente a una empresa.
     *
     * @param empresaId Identificador de la empresa a la cual se asociará el pago.
     * @param pagoId Identificador del pago a asociar.
     * @return Instancia de PagoEntity actualizada con la asociación.
     * @throws EntityNotFoundException si no se encuentra la empresa o el pago.
     * @throws IllegalOperationException si el pago ya está asociado a un caminante.
     */
    @Transactional
    public PagoEntity addPagoToEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de asociar el pago con id = {} a la empresa con id = {}", pagoId, empresaId);
        
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        }
        
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);
        }
        
        PagoEntity pago = pagoEntity.get();
        
        // Verificar que el pago no esté asociado a un caminante
        if (pago.getCaminante() != null) {
            throw new IllegalOperationException("El pago ya está asociado a un caminante");
        }
        
        pago.setEmpresa(empresaEntity.get());
        
        log.info("Termina proceso de asociar el pago con id = {} a la empresa con id = {}", pagoId, empresaId);
        return pagoRepository.save(pago);
    }

    /**
     * Obtiene la lista de pagos asociados a una empresa.
     *
     * @param empresaId Identificador de la empresa.
     * @return Lista de PagoEntity asociados a la empresa.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     */
    @Transactional
    public List<PagoEntity> getPagosEmpresa(Long empresaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar los pagos de la empresa con id = {}", empresaId);
        
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        }
        
        // Como no hay método findByEmpresaId en el repositorio, filtramos manualmente
        List<PagoEntity> todosPagos = pagoRepository.findAll();
        List<PagoEntity> pagosEmpresa = new ArrayList<>();
        
        for (PagoEntity pago : todosPagos) {
            if (pago.getEmpresa() != null && pago.getEmpresa().getId().equals(empresaId)) {
                pagosEmpresa.add(pago);
            }
        }
        
        log.info("Termina proceso de consultar los pagos de la empresa con id = {}", empresaId);
        return pagosEmpresa;
    }

    /**
     * Obtiene un pago específico asociado a una empresa.
     *
     * @param empresaId Identificador de la empresa.
     * @param pagoId Identificador del pago.
     * @return Instancia de PagoEntity.
     * @throws EntityNotFoundException si no se encuentra la empresa o el pago.
     * @throws IllegalOperationException si el pago no está asociado a la empresa.
     */
    @Transactional
    public PagoEntity getPagoEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);
        
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        }
        
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);
        }
        
        PagoEntity pago = pagoEntity.get();
        
        // Verificar que el pago esté asociado a la empresa
        if (pago.getEmpresa() == null || !pago.getEmpresa().getId().equals(empresaId)) {
            throw new IllegalOperationException("El pago no está asociado a la empresa");
        }
        
        log.info("Termina proceso de consultar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);
        return pago;
    }

    /**
     * Reemplaza los pagos asociados a una empresa.
     *
     * @param empresaId Identificador de la empresa.
     * @param pagos Lista de pagos para asociar a la empresa.
     * @return Lista de PagoEntity actualizados.
     * @throws EntityNotFoundException si no se encuentra la empresa o algún pago.
     * @throws IllegalOperationException si algún pago ya está asociado a un caminante.
     */
    @Transactional
    public List<PagoEntity> replacePagosEmpresa(Long empresaId, List<PagoEntity> pagos) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de reemplazar los pagos de la empresa con id = {}", empresaId);
        
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        }
        
        // Desasociar pagos existentes
        List<PagoEntity> pagosExistentes = getPagosEmpresa(empresaId);
        for (PagoEntity pagoExistente : pagosExistentes) {
            pagoExistente.setEmpresa(null);
            pagoRepository.save(pagoExistente);
        }
        
        // Asociar nuevos pagos
        EmpresaEntity empresa = empresaEntity.get();
        List<PagoEntity> nuevaListaPagos = new ArrayList<>();
        
        for (PagoEntity pago : pagos) {
            Optional<PagoEntity> pagoEntity = pagoRepository.findById(pago.getId());
            if (pagoEntity.isEmpty()) {
                throw new EntityNotFoundException("No se encontró el pago con id = " + pago.getId());
            }
            
            PagoEntity pagoActual = pagoEntity.get();
            if (pagoActual.getCaminante() != null) {
                throw new IllegalOperationException("El pago con id = " + pago.getId() + " ya está asociado a un caminante");
            }
            
            pagoActual.setEmpresa(empresa);
            nuevaListaPagos.add(pagoRepository.save(pagoActual));
        }
        
        log.info("Termina proceso de reemplazar los pagos de la empresa con id = {}", empresaId);
        return nuevaListaPagos;
    }

    /**
     * Elimina la asociación entre un pago y una empresa.
     *
     * @param empresaId Identificador de la empresa.
     * @param pagoId Identificador del pago.
     * @throws EntityNotFoundException si no se encuentra la empresa o el pago.
     * @throws IllegalOperationException si el pago no está asociado a la empresa.
     */
    @Transactional
    public void removePagoFromEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de desasociar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);
        
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        }
        
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);
        }
        
        PagoEntity pago = pagoEntity.get();
        
        // Verificar que el pago esté asociado a la empresa
        if (pago.getEmpresa() == null || !pago.getEmpresa().getId().equals(empresaId)) {
            throw new IllegalOperationException("El pago no está asociado a la empresa");
        }
        
        pago.setEmpresa(null);
        pagoRepository.save(pago);
        
        log.info("Termina proceso de desasociar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);
    }
}