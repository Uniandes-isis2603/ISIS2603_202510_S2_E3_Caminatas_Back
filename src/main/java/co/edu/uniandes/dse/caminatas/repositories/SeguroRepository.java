package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;

@Repository
public interface SeguroRepository extends JpaRepository<SeguroEntity, Long> {
    List<SeguroEntity> findByPago(PagoEntity pago);
}
