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

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Slf4j
@Transactional
@Import({CaminanteCaminataService.class})
public class CaminanteCaminataServiceTest {

    @Autowired
    private CaminanteCaminataService caminanteCaminataService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CaminanteEntity caminante = new CaminanteEntity();
    private List<CaminataEntity> caminatas = new ArrayList<>();

    @BeforeEach
    void setUp()
    {
        clearData();
        insertData();
    }

    private void clearData()
    {
        entityManager.getEntityManager().createQuery("delete from CaminanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
    }

    private void insertData()
    {
        caminante = factory.manufacturePojo(CaminanteEntity.class);
        entityManager.persist(caminante);

        for(int i = 0; i < 3; i++)
        {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata);
            caminata.getCaminantes().add(caminante);
            caminatas.add(caminata);
            caminante.getCaminatas().add(caminata);
        }
    }

    /*
     * Prueba para asociar una caminata a un caminante
     */
    @Test
    void testAddCaminata() throws EntityNotFoundException, IllegalOperationException
    {
        CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
        entityManager.persist(newCaminata);

        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        entityManager.persist(caminante);

        caminanteCaminataService.addCaminata(caminante.getId(), newCaminata.getId());

        CaminataEntity result = caminanteCaminataService.getCaminata(caminante.getId(), newCaminata.getId());
        assertEquals(newCaminata.getId(), result.getId());
        assertEquals(newCaminata.getNumero(), result.getNumero());
        assertEquals(newCaminata.getTitulo(), result.getTitulo());
        assertEquals(newCaminata.getTipo(), result.getTipo());
        assertEquals(newCaminata.getFecha(), result.getFecha());
        assertEquals(newCaminata.getHora(), result.getHora());
        assertEquals(newCaminata.getDepartamento(), result.getDepartamento());
        assertEquals(newCaminata.getCiudad(), result.getCiudad());
        assertEquals(newCaminata.getDuracionEstimadaMinutos(), result.getDuracionEstimadaMinutos());
    }

    /*
     * Prueba para asociar una caminata que no existe a un caminante
     */
    @Test
    void testAddInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            caminanteCaminataService.addCaminata(newCaminante.getId(), 0L);
        });
    }
    
    /*
     * Prueba para asociar una caminata a un caminante que no existe 
     */
    @Test
    void testAddCaminataInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(newCaminata);
            caminanteCaminataService.addCaminata(0L, newCaminata.getId());
        });
    }

    /*
     * Prueba para consultar la lista de caminatas de un caminante
     */
    @Test
    void testGetCaminatas() throws EntityNotFoundException
    {
        List<CaminataEntity> caminatasEntities = caminanteCaminataService.listCaminatas(caminante.getId());
        assertEquals(caminatas.size(), caminatasEntities.size());
        for(int i = 0; i < caminatas.size(); i++)
        {
            assertTrue(caminatasEntities.contains(caminatas.get(i)));
        }
    }

    /*
     * Prueba para consultar la lista de caminatas de un caminante que no existe
     */
    @Test
    void testGetCaminatasInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminanteCaminataService.listCaminatas(0L);
        });
    }

    /*
     * Prueba para consultar una caminata de un caminante 
     */
    @Test
    void testGetCaminata() throws EntityNotFoundException, IllegalOperationException
    {
        CaminataEntity caminataEntity = caminatas.get(0);
        CaminataEntity caminata = caminanteCaminataService.getCaminata(caminante.getId(), caminataEntity.getId());
        assertNotNull(caminata);

        assertEquals(caminataEntity.getId(), caminata.getId());
        // Add additional assertions for all relevant fields in CaminataEntity
    }

    /*
     * Prueba para consultar una caminata que no existe de un caminante
     */
    @Test
    void testGetInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminanteCaminataService.getCaminata(caminante.getId(), 0L);
        });
    }

    /*
     * Prueba para consultar una caminata de un caminante que no existe
     */
    @Test
    void testGetCaminataInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminataEntity caminataEntity = caminatas.get(0);
            caminanteCaminataService.getCaminata(0L, caminataEntity.getId());
        });
    }

    /*
     * Prueba para obtener una caminata no asociada a un caminante
     */
    @Test
    void testGetNotAssociatedCaminata()
    {
        assertThrows(IllegalOperationException.class, () -> 
        {
            CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(newCaminata);
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            caminanteCaminataService.getCaminata(newCaminante.getId(), newCaminata.getId());
        });
    }

    /*
     * Prueba para actualizar las caminatas de un caminante
     */
    @Test
    void testReplaceCaminatas() throws EntityNotFoundException
    {
        List<CaminataEntity> newCaminatas = new ArrayList<>();
        for(int i = 0; i < 3; i++)
        {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata);
            caminante.getCaminatas().add(caminata);
            newCaminatas.add(caminata);
        }
        caminanteCaminataService.replaceCaminatas(caminante.getId(), newCaminatas);

        List<CaminataEntity> caminatasEntities = caminanteCaminataService.listCaminatas(caminante.getId());
        for(CaminataEntity aNuevaLista: newCaminatas)
        {
            assertTrue(caminatasEntities.contains(aNuevaLista));
        }
    }

    /*
     * Prueba para actualizar las caminatas de un caminante que no existe
     */
    @Test
    void testReplaceCaminatasInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            List<CaminataEntity> newCaminatas = new ArrayList<>();
            for(int i = 0; i < 3; i++)
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.getCaminantes().add(caminante);
                entityManager.persist(caminata);
                newCaminatas.add(caminata);
            }
            caminanteCaminataService.replaceCaminatas(0L, newCaminatas);
        });
    }

    /*
     * Prueba para actualizar las caminatas que no existen de un caminante 
     */
    @Test
    void testReplaceInvalidCaminatas()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            List<CaminataEntity> newCaminatas = new ArrayList<>();
            for(int i = 0; i < 3; i++)
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setId(0L);
                newCaminatas.add(caminata);
            }
            caminanteCaminataService.replaceCaminatas(caminante.getId(), newCaminatas);
        });
    }

    /*
     * Prueba para desasociar una caminata de un caminante
     */
    @Test
    void testRemoveCaminata() throws EntityNotFoundException
    {
        for(CaminataEntity caminata: caminatas)
        {
            caminanteCaminataService.removeCaminata(caminante.getId(), caminata.getId());
        }
        assertTrue(caminanteCaminataService.listCaminatas(caminante.getId()).isEmpty());
    }

    /*
     * Prueba para desasociar una caminata de un caminante que no existe
     */
    @Test
    void testRemoveCaminataInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminanteCaminataService.removeCaminata(0L, caminatas.get(0).getId());
        });
    }

    /*
     * Prueba para desasociar una caminata que no existe de un caminante
     */
    @Test
    void testRemoveInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminanteCaminataService.removeCaminata(caminante.getId(), 0L);
        });
    }
}