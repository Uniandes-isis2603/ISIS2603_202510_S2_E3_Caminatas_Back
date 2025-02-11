package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;

@Repository
public interface CaminataRepository extends JpaRepository<CaminataEntity, Long> {
    
}
