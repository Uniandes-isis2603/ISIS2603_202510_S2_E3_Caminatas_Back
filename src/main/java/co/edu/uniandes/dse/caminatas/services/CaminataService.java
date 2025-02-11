package co.edu.uniandes.dse.caminatas.services;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;

public class CaminataService {

    @Autowired
    CaminataRepository caminataRepository;
}
