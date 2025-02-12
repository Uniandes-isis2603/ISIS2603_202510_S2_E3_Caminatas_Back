package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;

@Repository
public interface CaminanteRepository extends JpaRepository<CaminanteEntity, Long> {
    List<CaminanteEntity> findByDocumento(String documento);
}
