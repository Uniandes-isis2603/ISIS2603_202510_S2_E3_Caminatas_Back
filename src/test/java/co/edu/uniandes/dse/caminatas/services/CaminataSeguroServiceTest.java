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
@Import({CaminataSeguroService.class})
class CaminataSeguroServiceTest {

    @Autowired
    private CaminataSeguroService caminataSeguroService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminataEntity> listaCaminatas = new ArrayList<>();
    private List<SeguroEntity> listaSeguros = new ArrayList<>();

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
            listaCaminatas.add(caminata);
    
            for (int j = 0; j < 2; j++) {  // Asegura que se agreguen múltiples seguros
                SeguroEntity seguro = factory.manufacturePojo(SeguroEntity.class);
                seguro.setCaminata(caminata);
                entityManager.persist(seguro);
                listaSeguros.add(seguro);
            }
        }
    }
    
    @Test
    void testAddSeguroSuccess() throws EntityNotFoundException {
        CaminataEntity caminata = listaCaminatas.get(0);
        SeguroEntity seguro = listaSeguros.get(2); 
        SeguroEntity respuesta = caminataSeguroService.addSeguro(caminata.getId(), seguro.getId());
        assertNotNull(respuesta);
        assertEquals(seguro.getId(), respuesta.getId());
    }


    @Test
    void testAddSeguroCaminataNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.addSeguro(0L, listaSeguros.get(0).getId()));
    }

    @Test
    void testAddSeguroSeguroNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.addSeguro(listaCaminatas.get(0).getId(), 0L));
    }

    @Test
    void testGetSeguroSuccess() throws EntityNotFoundException, IllegalOperationException {
        CaminataEntity caminata = listaCaminatas.get(0);
        SeguroEntity seguro = listaSeguros.get(0);
        SeguroEntity respuesta = caminataSeguroService.getSeguro(caminata.getId(), seguro.getId());
        assertNotNull(respuesta);
        assertEquals(seguro.getId(), respuesta.getId());
    }

    @Test
    void testGetSeguroCaminataNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.getSeguro(0L, listaSeguros.get(0).getId()));
    }

    @Test
    void testGetSeguroSeguroNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.getSeguro(listaCaminatas.get(0).getId(), 0L));
    }

    @Test
    void testRemoveSeguroSuccess() throws EntityNotFoundException {
        CaminataEntity caminata = listaCaminatas.get(0);
        SeguroEntity seguro = listaSeguros.get(0);
        assertDoesNotThrow(() -> caminataSeguroService.removeSeguro(caminata.getId(), seguro.getId()));
    }

    @Test
    void testRemoveSeguroCaminataNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.removeSeguro(0L, listaSeguros.get(0).getId()));
    }

    @Test
    void testRemoveSeguroSeguroNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
            caminataSeguroService.removeSeguro(listaCaminatas.get(0).getId(), 0L));
    }
}
