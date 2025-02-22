package co.edu.uniandes.dse.caminatas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import co.edu.uniandes.dse.caminatas.repositories.EmpresaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataEmpresaService {
    
    @Autowired
    private CaminataRepository caminataRepository;

    @Autowired EmpresaRepository empresaRepository;

    /*
     * Remplazar la empresa de una caminata
     */
    @Transactional
    public CaminataEntity replaceEmpresa(Long caminataId, Long empresaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de remplazar la empresa de la caminata con id = {}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        Optional<EmpresaEntity> empresa = empresaRepository.findById(empresaId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }
        if(empresa.isEmpty())
        {
            throw new EntityNotFoundException("La empresa con id = " + empresaId + " no existe.");
        }
        caminata.get().setEmpresa(empresa.get());
        log.info("Termina proceso de remplazar la empresa de la caminata con id = {}", caminataId);
        return caminata.get();
    }

    /*
     * Eliminar la empresa de una caminata
     */
    @Transactional
    public void removeEmpresa(Long caminataId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de eliminar la empresa de la caminata con id = {}", caminataId);
        Optional<CaminataEntity> caminataOpt = caminataRepository.findById(caminataId);

        if (caminataOpt.isEmpty()) {
            throw new EntityNotFoundException("La caminata con id = " + caminataId + " no existe.");
        }

        CaminataEntity caminata = caminataOpt.get();
        EmpresaEntity empresa = caminata.getEmpresa();

        if (empresa != null) {
            empresa.getCaminatas().remove(caminata);
            caminata.setEmpresa(null);
        }

        log.info("Termina proceso de eliminar la empresa de la caminata con id = {}", caminataId);
    }

    
}
