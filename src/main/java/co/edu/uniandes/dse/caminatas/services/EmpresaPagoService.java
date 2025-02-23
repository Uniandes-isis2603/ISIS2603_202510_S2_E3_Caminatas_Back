package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.EmpresaRepository;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpresaPagoService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired 
    private PagoRepository pagoRepository;

    /**
     * Asocia un pago existente a una empresa
     */
    @Transactional
    public EmpresaEntity addPago(Long empresaId, Long pagoId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar un pago a la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);

        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);

        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);

        empresaEntity.get().getPagos().add(pagoEntity.get());
        log.info("Termina proceso de asociar un pago a la empresa con id = {}", empresaId);
        return empresaEntity.get();
    }

    /**
     * Obtiene una colección de instancias de PagoEntity asociadas a una instancia de Empresa
     */
    @Transactional
    public List<PagoEntity> getPagos(Long empresaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los pagos de la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);
        log.info("Finaliza proceso de consultar todos los pagos de la empresa con id = {}", empresaId);
        return empresaEntity.get().getPagos();
    }

    /**
     * Obtiene una instancia de PagoEntity asociada a una instancia de Empresa
     */
    @Transactional
    public PagoEntity getPago(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar un pago de la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);

        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);

        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);

        log.info("Termina proceso de consultar un pago de la empresa con id = {}", empresaId);
        
        if (!empresaEntity.get().getPagos().contains(pagoEntity.get()))
            throw new IllegalOperationException("El pago no está asociado a la empresa");

        return pagoEntity.get();
    }

    /**
     * Remplaza las instancias de PagoEntity asociadas a una instancia de Empresa
     */
    @Transactional
    public List<PagoEntity> replacePagos(Long empresaId, List<PagoEntity> pagos) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los pagos de la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);

        // Clear existing pagos
        empresaEntity.get().getPagos().clear();

        for (PagoEntity pago : pagos) {
            Optional<PagoEntity> pagoEntity = pagoRepository.findById(pago.getId());
            if (pagoEntity.isEmpty())
                throw new EntityNotFoundException("No se encontró el pago con id = " + pago.getId());

            empresaEntity.get().getPagos().add(pagoEntity.get());
        }
        log.info("Termina proceso de reemplazar los pagos de la empresa con id = {}", empresaId);
        return empresaEntity.get().getPagos();
    }

    /**
     * Desasocia un Pago existente de una Empresa existente
     */
    @Transactional
    public void removePago(Long empresaId, Long pagoId) throws EntityNotFoundException {
        log.info("Inicia proceso de remover un pago de la empresa con id = {}", empresaId);
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);

        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id = " + empresaId);

        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id = " + pagoId);

        empresaEntity.get().getPagos().remove(pagoEntity.get());
        log.info("Termina proceso de remover un pago de la empresa con id = {}", empresaId);
    }
}