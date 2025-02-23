package co.edu.uniandes.dse.caminatas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.EmpresaRepository;
import co.edu.uniandes.dse.caminatas.repositories.PagoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaPagoService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Transactional
    public PagoEntity addPago(Long empresaId, Long pagoId) throws EntityNotFoundException {
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id " + empresaId);

        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id " + pagoId);

        pagoEntity.get().setEmpresa(empresaEntity.get());
        empresaEntity.get().getPagos().add(pagoEntity.get());
        
        return pagoRepository.save(pagoEntity.get());
    }

    @Transactional
    public List<PagoEntity> getPagos(Long empresaId) throws EntityNotFoundException {
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id " + empresaId);
        
        return empresaEntity.get().getPagos();
    }

    @Transactional
    public PagoEntity getPago(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id " + empresaId);

        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id " + pagoId);

        if (!pagoEntity.get().getEmpresa().equals(empresaEntity.get()))
            throw new IllegalOperationException("El pago no está asociado con la empresa");

        return pagoEntity.get();
    }

    @Transactional
    public void removePago(Long empresaId, Long pagoId) throws EntityNotFoundException, IllegalOperationException {
        Optional<EmpresaEntity> empresaEntity = empresaRepository.findById(empresaId);
        if (empresaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró la empresa con id " + empresaId);

        Optional<PagoEntity> pagoEntity = pagoRepository.findById(pagoId);
        if (pagoEntity.isEmpty())
            throw new EntityNotFoundException("No se encontró el pago con id " + pagoId);

        if (!pagoEntity.get().getEmpresa().equals(empresaEntity.get()))
            throw new IllegalOperationException("El pago no está asociado con la empresa");

        empresaEntity.get().getPagos().remove(pagoEntity.get());
        pagoEntity.get().setEmpresa(null);
        pagoRepository.save(pagoEntity.get());
    }
}

