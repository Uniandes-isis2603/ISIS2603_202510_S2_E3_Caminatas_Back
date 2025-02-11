package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;

@Repository
public interface CaminataCompetenciaRepository extends JpaRepository<CaminataCompetenciaEntity, Long> {
    List<CaminataCompetenciaEntity> findByPatrocinador(PatrocinadorEntity patrocinador);
}
