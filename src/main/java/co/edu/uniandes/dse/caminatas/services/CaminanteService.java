package co.edu.uniandes.dse.caminatas.services;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import jakarta.transaction.Transactional;

public class CaminanteService {
    @Autowired
    CaminanteRepository caminanteRepository;

    @Transactional
    public CaminanteEntity createCaminante (CaminanteEntity caminante) throws IllegalOperationException{
        if (caminante.getDocumento().matches("\\d+")) {
            System.out.println("El documento contiene solo dígitos.");
        } else {
            System.out.println("El documento contiene caracteres no numéricos.");
        }
        return caminanteRepository.save(caminante);
    }
}
