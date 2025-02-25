package co.edu.uniandes.dse.caminatas.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminantePagoService 
{
    @Autowired
    private CaminanteRepository caminanteRepository;

    @Autowired
    private PagoRepository pagoRepository;

    /*
     * Agrega un pago al caminante.
     */
    @Transactional
    public CaminanteEntity addPago(Long caminanteID, Long pagoId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Agregando pago al usuario con ID {0}", caminanteID);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteID);
        Optional<PagoEntity> pago = pagoRepository.findById(pagoId);

        if(caminante.isEmpty())
        {
            
            throw new EntityNotFoundException("El caminante no existe.");
        }

        if(pago.isEmpty())
        {
            
            throw new EntityNotFoundException("Pago invalido.");
        }


        caminante.get().getPagos().add(pago.get());
        log.info("Agregando pago al usuario.");
        return caminante.get();
    }

    /*
     * Obtiene todos los pagos del caminante.
     */
    @Transactional
    public List<PagoEntity> listPagos(Long caminanteID) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Obteniendo lista de pagos del caminante con ID {0}", caminanteID);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteID);

        if(caminante.isEmpty())
        {
            throw new EntityNotFoundException("El caminante no existe.");
        }
        if(caminante.get().getPagos().isEmpty())
        {
            throw new IllegalOperationException("No hay pagos registrados del caminante.");
        }

        log.info("Lista de pagos del caminante.");
        return caminante.get().getPagos();
    }

    /*
     * Obtiene un pago asociado al caminante.
     */
    @Transactional
    public PagoEntity getPago(Long caminanteID, Long pagoId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Obteniendo lista de pagos del caminante con ID {0}", caminanteID);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteID);

        if(caminante.isEmpty())
        {
            throw new EntityNotFoundException("El caminante no existe.");
        }

        Optional<PagoEntity> pago = pagoRepository.findById(pagoId);
        if(pago.isEmpty())
        {
            throw new EntityNotFoundException("No hay pago registrado.");
        }

        if(!caminante.get().getPagos().contains(pago.get()))
        {
            throw new IllegalOperationException("El pago no está registrado en los pagos del caminante.");
        }

        log.info("Pago del caminante encontrado.");
        return pago.get();
    }

}