package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;

@Repository
public interface PatrocinadorRepository extends JpaRepository<PatrocinadorEntity, Long> {
    List<PatrocinadorEntity> findByDocumento(String documento);
}