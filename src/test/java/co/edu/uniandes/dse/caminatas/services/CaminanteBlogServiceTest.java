package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Slf4j
@Transactional
@Import({CaminanteBlogService.class})
public class CaminanteBlogServiceTest {

    @Autowired
    private CaminanteBlogService caminanteBlogService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private BlogEntity blog = new BlogEntity();
    private List<CaminanteEntity> caminantes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from BlogEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CaminanteEntity").executeUpdate();
    }

    private void insertData() {
        blog = factory.manufacturePojo(BlogEntity.class);
        entityManager.persist(blog);

        for (int i = 0; i < 3; i++) {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(caminante);
            blog.getCaminantesInteracciones().add(caminante);
            caminantes.add(caminante);
        }
    }

    /*
     * Prueba para asociar un caminante a un blog
     */
    @Test
    void testAddCaminante() throws EntityNotFoundException {
        CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
        entityManager.persist(newCaminante);

        caminanteBlogService.addCaminante(blog.getId(), newCaminante.getId());

        List<CaminanteEntity> caminantes = caminanteBlogService.listCaminantes(blog.getId());
        assertEquals(this.caminantes.size() + 1, caminantes.size());
        assertTrue(caminantes.contains(newCaminante));
    }

    /*
     * Prueba para asociar un caminante inválido a un blog
     */
    @Test
    void testAddInvalidCaminante() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogService.addCaminante(blog.getId(), 0L);
        });
    }

    /*
     * Prueba para asociar un caminante a un blog que no existe
     */
    @Test
    void testAddCaminanteInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            caminanteBlogService.addCaminante(0L, newCaminante.getId());
        });
    }

    /*
     * Prueba para consultar la lista de caminantes de un blog
     */
    @Test
    void testGetCaminantes() throws EntityNotFoundException {
        List<CaminanteEntity> caminantesEntities = caminanteBlogService.listCaminantes(blog.getId());
        assertEquals(caminantes.size(), caminantesEntities.size());
        for (int i = 0; i < caminantes.size(); i++) {
            CaminanteEntity caminante = caminantes.get(i);
            assertEquals(caminante.getId(), caminantesEntities.get(i).getId());
        }
    }

    /*
     * Prueba para consultar la lista de caminantes de un blog que no existe
     */
    @Test
    void testGetCaminantesInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogService.listCaminantes(0L);
        });
    }

    /*
     * Prueba para consultar un caminante de un blog
     */
    @Test
    void testGetCaminante() throws EntityNotFoundException, IllegalOperationException {
        CaminanteEntity caminanteEntity = caminantes.get(0);
        CaminanteEntity caminante = caminanteBlogService.getCaminante(blog.getId(), caminanteEntity.getId());
        assertNotNull(caminante);

        assertEquals(caminanteEntity.getId(), caminante.getId());
    }

    /*
     * Prueba para consultar un caminante que no existe de un blog
     */
    @Test
    void testGetInvalidCaminante() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogService.getCaminante(blog.getId(), 0L);
        });
    }

    /*
     * Prueba para consultar un caminante de un blog que no existe
     */
    @Test
    void testGetCaminanteInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            CaminanteEntity caminanteEntity = caminantes.get(0);
            caminanteBlogService.getCaminante(0L, caminanteEntity.getId());
        });
    }

    /*
     * Prueba para obtener un caminante no asociado a un blog
     */
    @Test
    void testGetNotAssociatedCaminante() {
        assertThrows(IllegalOperationException.class, () -> {
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            caminanteBlogService.getCaminante(blog.getId(), newCaminante.getId());
        });
    }

    /*
     * Prueba para actualizar los caminantes de un blog
     */
    @Test
    void testReplaceCaminantes() throws EntityNotFoundException {
        List<CaminanteEntity> newCaminantes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(caminante);
            newCaminantes.add(caminante);
        }
        caminanteBlogService.replaceCaminantes(blog.getId(), newCaminantes);

        List<CaminanteEntity> caminantesEntities = caminanteBlogService.listCaminantes(blog.getId());
        for (CaminanteEntity aNuevaLista : newCaminantes) {
            assertTrue(caminantesEntities.contains(aNuevaLista));
        }
    }

    /*
     * Prueba para actualizar los caminantes de un blog que no existe
     */
    @Test
    void testReplaceCaminantesInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<CaminanteEntity> newCaminantes = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
                entityManager.persist(caminante);
                newCaminantes.add(caminante);
            }
            caminanteBlogService.replaceCaminantes(0L, newCaminantes);
        });
    }

    /*
     * Prueba para actualizar los caminantes que no existen de un blog
     */
    @Test
    void testReplaceInvalidCaminantes() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<CaminanteEntity> newCaminantes = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
                caminante.setId(0L);
                newCaminantes.add(caminante);
                caminanteBlogService.replaceCaminantes(blog.getId(), newCaminantes);
            }
        });
    }

    /*
     * Prueba para desasociar un caminante de un blog
     */
    @Test
    void testRemoveCaminante() throws EntityNotFoundException {
        for (CaminanteEntity caminante : caminantes) {
            caminanteBlogService.removeCaminante(blog.getId(), caminante.getId());
        }
        assertTrue(caminanteBlogService.listCaminantes(blog.getId()).isEmpty());
    }

    /*
     * Prueba para desasociar un caminante de un blog que no existe
     */
    @Test
    void testRemoveCaminanteInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogService.removeCaminante(0L, caminantes.get(0).getId());
        });
    }

    /*
     * Prueba para desasociar un caminante que no existe de un blog
     */
    @Test
    void testRemoveInvalidCaminante() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogService.removeCaminante(blog.getId(), 0L);
        });
    }
}
