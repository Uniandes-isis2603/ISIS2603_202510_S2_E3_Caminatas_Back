package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Slf4j
@Transactional
@Import({CaminataCompetenciaPatrocinadorService.class})
public class CaminataCompetenciaPatrocinadorServiceTest {

    @Autowired
    private CaminataCompetenciaPatrocinadorService caminataCompetenciaPatrocinadorService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CaminataCompetenciaEntity caminataCompetencia;
    private PatrocinadorEntity patrocinador;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CaminataCompetenciaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PatrocinadorEntity").executeUpdate();
    }

    private void insertData() {
        caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
        entityManager.persist(caminataCompetencia);

        patrocinador = factory.manufacturePojo(PatrocinadorEntity.class);
        entityManager.persist(patrocinador);

        caminataCompetencia.setPatrocinador(patrocinador);
        patrocinador.getCaminatasCompetencia().add(caminataCompetencia);
        entityManager.persist(caminataCompetencia);
    }

    /*
     * Prueba para reemplazar el patrocinador de una caminata de competencia
     */
    @Test
    void testReplacePatrocinador() throws EntityNotFoundException {
        PatrocinadorEntity newPatrocinador = factory.manufacturePojo(PatrocinadorEntity.class);
        entityManager.persist(newPatrocinador);

        caminataCompetenciaPatrocinadorService.replacePatrocinador(caminataCompetencia.getId(), newPatrocinador.getId());

        CaminataCompetenciaEntity updatedCaminata = entityManager.find(CaminataCompetenciaEntity.class, caminataCompetencia.getId());
        assertEquals(newPatrocinador.getId(), updatedCaminata.getPatrocinador().getId());
    }

    /*
     * Prueba para reemplazar el patrocinador de una caminata de competencia que no existe
     */
    @Test
    void testReplacePatrocinadorInvalidCaminata() {
        PatrocinadorEntity newPatrocinador = factory.manufacturePojo(PatrocinadorEntity.class);
        entityManager.persist(newPatrocinador);

        assertThrows(EntityNotFoundException.class, () -> {
            caminataCompetenciaPatrocinadorService.replacePatrocinador(0L, newPatrocinador.getId());
        });
    }

    /*
     * Prueba para reemplazar el patrocinador con uno que no existe
     */
    @Test
    void testReplaceInvalidPatrocinador() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminataCompetenciaPatrocinadorService.replacePatrocinador(caminataCompetencia.getId(), 0L);
        });
    }

    /*
     * Prueba para borrar el patrocinador de una caminata de competencia
     */
    @Test
    void testRemovePatrocinador() throws EntityNotFoundException {
        caminataCompetenciaPatrocinadorService.removePatrocinador(caminataCompetencia.getId());

        CaminataCompetenciaEntity updatedCaminata = entityManager.find(CaminataCompetenciaEntity.class, caminataCompetencia.getId());
        assertNull(updatedCaminata.getPatrocinador());
    }

    /*
     * Prueba para borrar el patrocinador de una caminata de competencia que no existe
     */
    @Test
    void testRemovePatrocinadorInvalidCaminata() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminataCompetenciaPatrocinadorService.removePatrocinador(0L);
        });
    }
}
