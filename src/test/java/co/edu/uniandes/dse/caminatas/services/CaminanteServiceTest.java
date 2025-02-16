package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

    @DataJpaTest
    @Transactional
    @Import(CaminanteService.class)
    class CaminanteServiceTest {
        @Autowired
        private CaminanteService caminanteService;

        @Autowired
        private TestEntityManager entityManager;

        private PodamFactory factory = new PodamFactoryImpl();

        private List<CaminanteEntity> caminanteList = new ArrayList<>();

    @Test
    void createCaminanteTest() throws IllegalOperationException {
        CaminanteEntity newEntity = factory.manufacturePojo(CaminanteEntity.class);
        CaminanteEntity result = caminanteService.createCaminante(newEntity);
        assertNotNull(result);

        CaminanteEntity entity = entityManager.find(CaminanteEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getDocumento(), entity.getDocumento());
    }
}
