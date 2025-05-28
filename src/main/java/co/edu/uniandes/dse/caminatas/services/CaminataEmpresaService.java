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

    private static final String MENSAJE_1 = "La caminata con id = ";
    private static final String MENSAJE_2 = " no existe.";
    private static final String MENSAJE_3 = "La empresa con id = ";

    @Autowired
    private CaminataRepository caminataRepository;

    @Autowired 
    private EmpresaRepository empresaRepository;

    @Transactional
    public CaminataEntity addEmpresa(Long caminataId, Long empresaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de agregar una empresa a la caminata con id = {}", caminataId);

        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        Optional<EmpresaEntity> empresa = empresaRepository.findById(empresaId);

        if (caminata.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + caminataId + MENSAJE_2);
        }
        if (empresa.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + empresaId + MENSAJE_2);
        }

        caminata.get().setEmpresa(empresa.get());
        log.info("Termina proceso de agregar una empresa a la caminata con id = {}", caminataId);

        return caminata.get();  
    }

    @Transactional
    public EmpresaEntity getEmpresa(Long caminataId, Long empresaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de obtener la empresa de la caminata con id = {0}", caminataId);
        Optional<CaminataEntity> caminata = caminataRepository.findById(caminataId);
        if (caminata.isEmpty())
        {
            throw new EntityNotFoundException(MENSAJE_1 + caminataId + MENSAJE_2);
        }
        log.info("Termina proceso de obtener la empresa de la caminata con id = {0}", caminataId);
        return caminata.get().getEmpresa();
    }

}
