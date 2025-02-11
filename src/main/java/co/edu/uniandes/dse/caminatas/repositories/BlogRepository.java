package co.edu.uniandes.dse.caminatas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByTitulo(String titulo);
}
