package co.edu.uniandes.dse.caminatas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service

public class SeguroService {
    @Autowired
    SeguroRepository seguroRepository;

    @Transactional
    public SeguroEntity createSeguro(SeguroEntity entity) throws EntityNotFoundException {
        log.info("Inicia el proceso de creación de un seguro");
        return seguroRepository.save(entity);
        log.info("Finaliza el proceso de creación de un seguro");
    }

    @Transactional
    public SeguroEntity updateSeguro(SeguroEntity entity) {
        log.info("Inicia el proceso de actualización de un seguro");
        return seguroRepository.save(entity);
        log.info("Finaliza el proceso de actualización de un seguro");
    }

    @Transactional
    public void deleteSeguro(Long id) {
        log.info("Inicia el proceso de eliminación de un seguro");
        seguroRepository.deleteById(id);
        log.info("Finaliza el proceso de eliminación de un seguro");
    }

}