package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(SeguroService.class)
class SeguroServiceTest {
    @Autowired
    private SeguroService seguroService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<SeguroEntity> seguroList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SeguroEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            SeguroEntity seguroEntity = factory.manufacturePojo(SeguroEntity.class);
            entityManager.persist(seguroEntity);
            seguroList.add(seguroEntity);
        }
    }

    @Test
    void testCreateSeguro() throws EntityNotFoundException, IllegalOperationException {
        SeguroEntity seguro = factory.manufacturePojo(SeguroEntity.class);
        SeguroEntity result = seguroService.createSeguro(seguro);
        assertNotNull(result);

        SeguroEntity entity = entityManager.find(SeguroEntity.class, result.getId());
        assertEquals(seguro.getNombre(), entity.getNombre());
        assertEquals(seguro.getTipo(), entity.getTipo());
        assertEquals(seguro.getNumero(), entity.getNumero());
    }

    @Test
    void testCreateSeguroWithInvalidData() {
        assertThrows(IllegalOperationException.class, () -> {
            SeguroEntity seguro = factory.manufacturePojo(SeguroEntity.class);
            seguro.setNombre(null);
            seguroService.createSeguro(seguro);
        });
    }

    @Test
    void testGetSeguro() throws EntityNotFoundException {
        SeguroEntity entity = seguroList.get(0);
        SeguroEntity resultEntity = seguroService.getSeguro(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getTipo(), resultEntity.getTipo());
    }

    @Test
    void testGetInvalidSeguro() {
        assertThrows(EntityNotFoundException.class, () -> {
            seguroService.getSeguro(0L);
        });
    }

    @Test
    void testUpdateSeguro() throws EntityNotFoundException, IllegalOperationException {
        SeguroEntity entity = seguroList.get(0);
        SeguroEntity pojoEntity = factory.manufacturePojo(SeguroEntity.class);
        pojoEntity.setId(entity.getId());
        seguroService.updateSeguro(entity.getId(), pojoEntity);

        SeguroEntity resp = entityManager.find(SeguroEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getTipo(), resp.getTipo());
    }

    @Test
    void testUpdateInvalidSeguro() {
        assertThrows(EntityNotFoundException.class, () -> {
            SeguroEntity pojoEntity = factory.manufacturePojo(SeguroEntity.class);
            pojoEntity.setId(0L);
            seguroService.updateSeguro(0L, pojoEntity);
        });
    }

    @Test
    void testDeleteSeguro() throws EntityNotFoundException {
        SeguroEntity entity = seguroList.get(1);
        seguroService.deleteSeguro(entity.getId());
        SeguroEntity deleted = entityManager.find(SeguroEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidSeguro() {
        assertThrows(EntityNotFoundException.class, () -> {
            seguroService.deleteSeguro(0L);
        });
    }
}
