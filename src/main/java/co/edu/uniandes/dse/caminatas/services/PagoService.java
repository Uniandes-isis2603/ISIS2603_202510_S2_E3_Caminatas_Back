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
    List<PagoEntity> pagos1 = new ArrayList<PagoEntity>();

    @Autowired
	PagoRepository pagoRepository;

    /**
     * Crea una nueva Empresa en la base de datos.
     *
     * Reglas de negocio:
     * - El numero de transacción no se puede repetir.
     * - Los valores del pago no pueden ser menores a 0.
     * - La tarjeta y el codigo CCV deben tener una cantidad de digitos especifica.
     * - El pago debe contar con una caminata.
     * - El pago debe contar con un usuario, ya sea una empresa o caminante.
     * - El pago no pueden contar con dos usuarios.
     *
     * @param pago la entidad con los datos nuevos.
     * @return la entidad en la base de datos guardada.
     * @throws IllegalOperationException si se incumple alguna regla de negocio.
     * @throws EntityNotFoundException si falta una entidad en el pago.
     */
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

    /**
     * Obtiene la lista de todas los pagos.
     *
     * @return Lista de PagoEntity.
     */
    @Transactional
    public List<PagoEntity> getPagos ()
    {
        log.info("Mostrando pagos");
        List<PagoEntity> pagos = pagoRepository.findAll();
        return pagos;
    }
    
    /**
     * Obtiene los datos de un pago a partir de su ID.
     *
     * @param id Identificador de la instancia a consultar.
     * @return Instancia de PagoEntity con los datos consultados.
     * @throws EntityNotFoundException si no se encuentra el pago.
     */
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
