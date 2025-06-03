package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private static final String MENSAJE_1 = "No se encontró la empresa con el id = ";
    private static final String MENSAJE_2 = "No se encontró el pago con el id = ";

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired 
    private PagoRepository pagoRepository;

    private EmpresaEntity getEmpresaOrThrow(Long empresaId) throws EntityNotFoundException {
    return empresaRepository.findById(empresaId)
        .orElseThrow(() -> new EntityNotFoundException(MENSAJE_1 + empresaId));
    }

    private PagoEntity getPagoOrThrow(Long pagoId) throws EntityNotFoundException {
        return pagoRepository.findById(pagoId)
            .orElseThrow(() -> new EntityNotFoundException(MENSAJE_2 + pagoId));
    }

    /**
     * Asocia un pago existente a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @return Instancia de PagoEntity que fue asociada a la empresa
     * @throws EntityNotFoundException Si la empresa o el pago no existen
     * @throws IllegalOperationException Si el pago ya está asociado a un caminante
     */
    @Transactional
    public PagoEntity addPagoToEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de asociar el pago con id = {} a la empresa con id = {}", pagoId, empresaId);

        EmpresaEntity empresa = getEmpresaOrThrow(empresaId);
        PagoEntity pago = getPagoOrThrow(pagoId);

        // Verificar que el pago no esté asociado a un caminante
        if (pago.getCaminante() != null) {
            throw new IllegalOperationException("El pago ya está asociado a un caminante");
        }

        pago.setEmpresa(empresa);
        log.info("Termina proceso de asociar el pago con id = {} a la empresa con id = {}", pagoId, empresaId);

        return pagoRepository.save(pago);
    }

    /**
     * Obtiene un pago asociado a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @return Instancia de PagoEntity asociada a la empresa
     * @throws EntityNotFoundException Si la empresa o el pago no existen o si el pago no está asociado a la empresa
     */
    @Transactional
    public PagoEntity getPagoFromEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException {
    log.info("Inicia proceso de consultar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);

    EmpresaEntity empresa = getEmpresaOrThrow(empresaId);
    PagoEntity pago = getPagoOrThrow(pagoId);

    // Verificar que el pago esté asociado a la empresa
    if (pago.getEmpresa() == null || !pago.getEmpresa().getId().equals(empresa.getId())) {
        throw new EntityNotFoundException("El pago con id = " + pagoId + " no está asociado a la empresa con id = " + empresaId);
    }

    log.info("Termina proceso de consultar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);

    return pago;
    }

    /**
     * Obtiene la lista de pagos asociados a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @return Lista de instancias de PagoEntity asociadas a la empresa
     * @throws EntityNotFoundException Si la empresa no existe
     */
    @Transactional
    public List<PagoEntity> getPagosFromEmpresa(Long empresaId) throws EntityNotFoundException {
    log.info("Inicia proceso de consultar todos los pagos de la empresa con id = {}", empresaId);

    EmpresaEntity empresa = getEmpresaOrThrow(empresaId);

    // Obtener todos los pagos y filtrar por empresa
    List<PagoEntity> allPagos = pagoRepository.findAll();
    List<PagoEntity> empresaPagos = allPagos.stream()
        .filter(pago -> pago.getEmpresa() != null && pago.getEmpresa().getId().equals(empresaId))
        .collect(Collectors.toList());

    log.info("Termina proceso de consultar todos los pagos de la empresa con id = {}", empresaId);

    return empresaPagos;
    }

    /**
     * Desasocia un pago de una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @throws EntityNotFoundException Si la empresa o el pago no existen o si el pago no está asociado a la empresa
     */
    @Transactional
    public void removePagoFromEmpresa(Long empresaId, Long pagoId) throws EntityNotFoundException {
    log.info("Inicia proceso de desasociar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);

    EmpresaEntity empresa = getEmpresaOrThrow(empresaId);
    PagoEntity pago = getPagoOrThrow(pagoId);

    // Verificar que el pago esté asociado a la empresa
    if (pago.getEmpresa() == null || !pago.getEmpresa().getId().equals(empresa.getId())) {
        throw new EntityNotFoundException("El pago con id = " + pagoId + " no está asociado a la empresa con id = " + empresaId);
    }

    // Desasociar el pago de la empresa
    pago.setEmpresa(null);
    pagoRepository.save(pago);

    log.info("Termina proceso de desasociar el pago con id = {} de la empresa con id = {}", pagoId, empresaId);
}
}