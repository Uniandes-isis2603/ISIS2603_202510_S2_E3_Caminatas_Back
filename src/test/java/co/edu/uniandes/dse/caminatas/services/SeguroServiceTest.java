package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jakarta.transaction.Transactional;


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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({SeguroService.class, CaminataService.class})
class SeguroServiceTest {
    @Autowired
    private SeguroService seguroService;

    @Autowired
    private CaminataService caminataService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<SeguroEntity> seguroList = new ArrayList<SeguroEntity>();
    private List<CaminataEntity> caminataList = new ArrayList<>();

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
            CaminataEntity caminataEntity = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminataEntity);
            seguroEntity.setCaminata(caminataEntity);
            caminataEntity.setSeguro(seguroEntity);
            entityManager.persist(seguroEntity);
            seguroList.add(seguroEntity);
            caminataList.add(caminataEntity);
        }
    }

    @Test
    void testCreateSeguro() throws EntityNotFoundException, IllegalOperationException {
        SeguroEntity newEntity = factory.manufacturePojo(SeguroEntity.class);
        CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1 + (int)(Math.random() * 365)); // Genera una fecha aleatoria dentro del próximo año
        newCaminata.setFecha(calendar.getTime());

        LocalTime hora = LocalTime.now().plusHours(1+ (int)(Math.random() * 12)); 
        newCaminata.setHora(hora);

        newCaminata.setDepartamento("Cundinamarca");
        newCaminata.setCiudad("Bogotá");
        newCaminata.setDuracionEstimadaMinutos(120);

        newCaminata = caminataService.createCaminata(newCaminata);
        newEntity.setCaminata(newCaminata);
        SeguroEntity result = seguroService.createSeguro(newEntity);
        assertNotNull(result);
        SeguroEntity entity = entityManager.find(SeguroEntity.class, result.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getTipo(), entity.getTipo());
        assertEquals(newEntity.getNumero(), entity.getNumero());
        assertEquals(newEntity.getId(), entity.getId());
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
