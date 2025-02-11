package co.edu.uniandes.dse.caminatas.services;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import jakarta.transaction.Transactional;

public class CaminataService {

    @Autowired
    CaminataRepository caminataRepository;

    //@Transactional
    //public CaminataEntity createCaminata (CaminataEntity caminata) throws IllegalOperationException{
        

    //}
}
