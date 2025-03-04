package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataPagoService 
{
    @Autowired
    private CaminataRepository caminataRepository;

    @Autowired
    private PagoRepository pagoRepository;

    /*
     * Agrega un pago a la caminata
     */
    @Transactional
    public PagoEntity addPago(Long caminataId, Long pagoId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de agregar un pago a la caminata con id = {}", caminataId);
        
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        Optional<PagoEntity> pago = pagoRepository.findById(pagoId);
        
        if (caminata.isEmpty()) {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        if (pago.isEmpty()) {
            throw new EntityNotFoundException("El pago con id = " + pagoId + " no existe.");
        }
        
        caminata.get().getPagos().add(pago.get());
        log.info("Termina proceso de agregar un pago a la caminata con id = {}", caminataId);
        
        return pago.get();  
    }

    /*
     * Retorna todos los pagos asociados a una caminata
     */
    @Transactional
    public List<PagoEntity> listPagos(Long caminataId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de obtener los pagos de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        log.info("Termina proceso de obtener los pagos de la caminata con id = {0}", caminataId);
        return caminata.get().getPagos();
    }

    /*
     * Retorna un pago asociado a una caminata
     */
    @Transactional
    public PagoEntity getPago(Long caminataId, Long pagoId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de obtener un pago de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        Optional<PagoEntity> pago = pagoRepository.findById(pagoId);
        if (pago.isEmpty())
        {
            throw new EntityNotFoundException("El pago con id = " + pagoId + " no existe.");
        }
        if (!caminata.get().getPagos().contains(pago.get())) {
            throw new IllegalOperationException("El pago con id = " + pagoId + " no está asociado a la caminata con id = " + caminataId);
        }
        log.info("Termina proceso de obtener un pago de la caminata con id = {0}", caminataId);
        return pago.get();
    }

}
