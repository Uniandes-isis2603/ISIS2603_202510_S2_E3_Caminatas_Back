package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;

@Repository
public interface SeguroRepository extends JpaRepository<SeguroEntity, Long> {
    @Query("SELECT s FROM SeguroEntity s JOIN s.caminata c JOIN c.pagos p WHERE p = :pago")
    List<SeguroEntity> findByPago(@Param("pago") PagoEntity pago);
}
