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

@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepository;

    @Transactional
    public EmpresaEntity createEmpresa(EmpresaEntity empresa) throws IllegalOperationException {
        validateEmpresa(empresa);
        return empresaRepository.save(empresa);
    }

    @Transactional(readOnly = true)
    public List<EmpresaEntity> getEmpresas() {
        return empresaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmpresaEntity getEmpresa(Long empresaId) throws EntityNotFoundException {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    @Transactional
    public EmpresaEntity updateEmpresa(Long empresaId, EmpresaEntity empresa) throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity existingEmpresa = getEmpresa(empresaId);
        validateEmpresa(empresa);
        empresa.setId(existingEmpresa.getId());
        return empresaRepository.save(empresa);
    }

    @Transactional
    public void deleteEmpresa(Long empresaId) throws EntityNotFoundException {
        if (!empresaRepository.existsById(empresaId)) {
            throw new EntityNotFoundException("Empresa no encontrada");
        }
        empresaRepository.deleteById(empresaId);
    }

    private void validateEmpresa(EmpresaEntity empresa) throws IllegalOperationException {
        if (empresa.getNombre() == null || empresa.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre de la empresa no puede ser vacío");
        }
        if (empresa.getDocumento() <= 0) {
            throw new IllegalOperationException("El documento de la empresa debe ser un número positivo");
        }
        if (empresa.getCorreo() == null || empresa.getCorreo().isEmpty()) {
            throw new IllegalOperationException("El correo de la empresa no puede ser vacío");
        }
    }
}

