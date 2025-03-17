package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CaminataSeguroService.class)
class CaminataSeguroServiceTest {

    @Autowired
    private CaminataSeguroService caminataSeguroService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminataEntity> caminatas = new ArrayList<>();
    private List<SeguroEntity> seguros = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SeguroEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata);
            caminatas.add(caminata);
        }

        for (int i = 0; i < 3; i++) {
            SeguroEntity seguro = factory.manufacturePojo(SeguroEntity.class);
            // No establecemos la relación con caminata aquí para evitar restricciones de unicidad
            entityManager.persist(seguro);
            seguros.add(seguro);
        }
    }

    @Test
    void testAddSeguroSuccess() throws EntityNotFoundException {
        CaminataEntity caminata = caminatas.get(0);
        SeguroEntity seguro = seguros.get(0);
        
        SeguroEntity result = caminataSeguroService.addSeguro(caminata.getId(), seguro.getId());
        
        assertNotNull(result);
        assertEquals(seguro.getId(), result.getId());
        assertEquals(seguro.getNombre(), result.getNombre());
    }

    @Test
    void testAddSeguroCaminataNotFound() {
        Long nonExistentCaminataId = 0L;
        Long seguroId = seguros.get(0).getId();
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.addSeguro(nonExistentCaminataId, seguroId);
        });
    }

    @Test
    void testAddSeguroSeguroNotFound() {
        Long caminataId = caminatas.get(0).getId();
        Long nonExistentSeguroId = 0L;
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.addSeguro(caminataId, nonExistentSeguroId);
        });
    }

    @Test
    void testGetSeguroSuccess() throws EntityNotFoundException, IllegalOperationException {
        CaminataEntity caminata = caminatas.get(0);
        SeguroEntity seguro = seguros.get(0);
        
        // Primero agregamos el seguro a la caminata
        caminataSeguroService.addSeguro(caminata.getId(), seguro.getId());
        
        // Luego obtenemos el seguro
        SeguroEntity result = caminataSeguroService.getSeguro(caminata.getId(), seguro.getId());
        
        assertNotNull(result);
        assertEquals(seguro.getId(), result.getId());
        assertEquals(seguro.getNombre(), result.getNombre());
    }

    @Test
    void testGetSeguroCaminataNotFound() {
        Long nonExistentCaminataId = 0L;
        Long seguroId = seguros.get(0).getId();
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.getSeguro(nonExistentCaminataId, seguroId);
        });
    }

    @Test
    void testGetSeguroSeguroNotFound() {
        Long caminataId = caminatas.get(0).getId();
        Long nonExistentSeguroId = 0L;
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.getSeguro(caminataId, nonExistentSeguroId);
        });
    }

    @Test
    void testRemoveSeguroSuccess() throws EntityNotFoundException {
        CaminataEntity caminata = caminatas.get(0);
        SeguroEntity seguro = seguros.get(0);
        
        // Primero agregamos el seguro a la caminata
        caminataSeguroService.addSeguro(caminata.getId(), seguro.getId());
        
        // Luego lo removemos
        assertDoesNotThrow(() -> {
            caminataSeguroService.removeSeguro(caminata.getId(), seguro.getId());
        });
    }

    @Test
    void testRemoveSeguroCaminataNotFound() {
        Long nonExistentCaminataId = 0L;
        Long seguroId = seguros.get(0).getId();
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.removeSeguro(nonExistentCaminataId, seguroId);
        });
    }

    @Test
    void testRemoveSeguroSeguroNotFound() {
        Long caminataId = caminatas.get(0).getId();
        Long nonExistentSeguroId = 0L;
        
        assertThrows(EntityNotFoundException.class, () -> {
            caminataSeguroService.removeSeguro(caminataId, nonExistentSeguroId);
        });
    }
}
