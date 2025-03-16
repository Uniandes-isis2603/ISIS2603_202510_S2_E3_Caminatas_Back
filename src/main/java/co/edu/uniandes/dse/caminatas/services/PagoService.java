package co.edu.uniandes.dse.caminatas.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PagoService 
{
    List<PagoEntity> pagos = new ArrayList<PagoEntity>();

    @Autowired
	PagoRepository pagoRepository;

    @Transactional
    public PagoEntity createPago (PagoEntity pago) throws EntityNotFoundException, IllegalOperationException
    {

    
        log.info("Creando pago");
        
        if(!pagoRepository.findByNumeroTransaccion(pago.getNumeroTransaccion()).isEmpty())
        {
            throw new IllegalOperationException("Ya existe registro de este pago");
        }
        if(pago.getValorCaminata() <=0 || pago.getValorTotal() <=0)
        {
            throw new IllegalOperationException("El valor pagado no es valido");
        }
        if(pago.getNumeroTarjeta().strip().length()!=16 || pago.getCcv().strip().length() != 3)
        {
            throw new IllegalOperationException("Tarjeta invalida");
        }
        if(pago.getCaminata() == null)
        {
            throw new EntityNotFoundException("Pago no asociado a ninguna caminata");
        }
        if(pago.getEmpresa() == null && pago.getCaminante() == null)
        {
            throw new EntityNotFoundException("Tarjeta no asociada a ningun usuario");
        }
        if(pago.getEmpresa() != null && pago.getCaminante() != null)
        {
            throw new IllegalOperationException("Tarjeta asociada a dos usuarios");
        }
        log.info("Pago creado con id = {0}");
        return pagoRepository.save(pago);
    }

    @Transactional
    public List<PagoEntity> getPagos ()
    {
        log.info("Mostrando pagos");
        List<PagoEntity> pagos = pagoRepository.findAll();
        return pagos;
    }
    
    @Transactional
    public PagoEntity getPago (Long id) throws EntityNotFoundException
    {
        log.info("Buscando pago con el id = {0}", id);
		Optional<PagoEntity> pagoEntity = pagoRepository.findById(id);
		if(pagoEntity.isEmpty())
        {
            throw new EntityNotFoundException("El pago no existe");
        }
		log.info("Pago encontrado con id = {0}", id);
		return pagoEntity.get();
    }
    
}
