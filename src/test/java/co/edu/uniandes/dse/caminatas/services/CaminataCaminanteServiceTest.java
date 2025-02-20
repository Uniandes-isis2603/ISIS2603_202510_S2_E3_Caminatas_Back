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
@Import({CaminataCaminanteService.class})
public class CaminataCaminanteServiceTest {

    @Autowired
    private CaminataCaminanteService caminataCaminanteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CaminataEntity caminata = new CaminataEntity();
    private List<CaminanteEntity> caminantes = new ArrayList<>();

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
        caminata = factory.manufacturePojo(CaminataEntity.class);
        entityManager.persist(caminata);

        for(int i = 0; i < 3; i++)
        {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(caminante);
            caminante.getCaminatas().add(caminata);
            caminantes.add(caminante);
            caminata.getCaminantes().add(caminante);
        }
    }

    /*
     * Prueba para asociar un caminante a una caminata
     */
    @Test
    void testAddCaminante() throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
        entityManager.persist(newCaminante);

        CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
        entityManager.persist(caminata);

        caminataCaminanteService.addCaminante(caminata.getId(), newCaminante.getId());

        CaminanteEntity lastCaminante = caminataCaminanteService.getCaminante(caminata.getId(), newCaminante.getId());
        assertEquals(newCaminante.getId(), lastCaminante.getId());
        assertEquals(newCaminante.getNombre(), lastCaminante.getNombre());
        assertEquals(newCaminante.getCorreo(), lastCaminante.getCorreo());
        assertEquals(newCaminante.getTelefono(), lastCaminante.getTelefono());
        assertEquals(newCaminante.getDireccion(), lastCaminante.getDireccion());
        assertEquals(newCaminante.isExperienciaPrevia(), lastCaminante.isExperienciaPrevia());
        assertEquals(newCaminante.isTratamientosMed(), lastCaminante.isTratamientosMed());
        assertEquals(newCaminante.isLesion(), lastCaminante.isLesion());
        assertEquals(newCaminante.isProblemasRes(), lastCaminante.isProblemasRes());

    }

    /*
     * Prueba para asociar un caminante a una caminata
     */
    @Test
    void testAddInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(newCaminata);
            caminataCaminanteService.addCaminante(newCaminata.getId(), 0L);
        });
    }
    

    /*
     * Prueba para asociar un caminante a una caminata que no existe 
     */
    @Test
    void testAddCaminanteInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            caminataCaminanteService.addCaminante(0L, newCaminante.getId());
        });
    }

    /*
     * Prueba para consultar la lista de caminantes de una caminata
     */
    @Test
    void testGetCaminantes() throws EntityNotFoundException
    {
        List<CaminanteEntity> caminantesEntities = caminataCaminanteService.listCaminantes(caminata.getId());
        assertEquals(caminantes.size(), caminantesEntities.size());
        for(int i = 0; i < caminantes.size(); i++)
        {
            CaminanteEntity caminante = caminantes.get(i);
            assertEquals(caminante.getId(), this.caminantes.get(i).getId());
            assertEquals(caminante.getNombre(), this.caminantes.get(i).getNombre());
            assertEquals(caminante.getCorreo(), this.caminantes.get(i).getCorreo());
            assertEquals(caminante.getTelefono(), this.caminantes.get(i).getTelefono());
            assertEquals(caminante.getDireccion(), this.caminantes.get(i).getDireccion());
            assertEquals(caminante.isExperienciaPrevia(), this.caminantes.get(i).isExperienciaPrevia());
            assertEquals(caminante.isTratamientosMed(), this.caminantes.get(i).isTratamientosMed());
            assertEquals(caminante.isLesion(), this.caminantes.get(i).isLesion());
            assertEquals(caminante.isProblemasRes(), this.caminantes.get(i).isProblemasRes());
        }
    }

    /*
     * Prueba para consultar la lista de caminantes de una caminata que no existe
     */
    @Test
    void testGetCaminantesInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminataCaminanteService.listCaminantes(0L);
        });
    }


    /*
     * Prueba para consultar un caminante de una caminata 
     */
    @Test
    void testGetCaminante() throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity caminanteEntity = caminantes.get(0);
        CaminanteEntity caminante = caminataCaminanteService.getCaminante(caminata.getId(), caminanteEntity.getId());
        assertNotNull(caminante);

        assertEquals(caminanteEntity.getId(), caminante.getId());
        assertEquals(caminanteEntity.getNombre(), caminante.getNombre());
        assertEquals(caminanteEntity.getCorreo(), caminante.getCorreo());
        assertEquals(caminanteEntity.getTelefono(), caminante.getTelefono());
        assertEquals(caminanteEntity.getDireccion(), caminante.getDireccion());
        assertEquals(caminanteEntity.isExperienciaPrevia(), caminante.isExperienciaPrevia());
        assertEquals(caminanteEntity.isTratamientosMed(), caminante.isTratamientosMed());
        assertEquals(caminanteEntity.isLesion(), caminante.isLesion());
        assertEquals(caminanteEntity.isProblemasRes(), caminante.isProblemasRes());
    }

    /*
     * Prueba para consultar un caminante que no existe de una caminata
     */
    @Test
    void testGetInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminataCaminanteService.getCaminante(caminata.getId(), 0L);
        });
    }

    /*
     * Prueba para consultar un caminante de una caminata que no existe
     */
    @Test
    void testGetCaminanteInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            CaminanteEntity caminanteEntity = caminantes.get(0);
            caminataCaminanteService.getCaminante(0L, caminanteEntity.getId());
        });
    }

    /*
     * Prueba para obtener un caminante no asociado a una caminata
     */
    @Test
    void testGetNotAssociatedCaminante()
    {
        assertThrows(IllegalOperationException.class, () -> 
        {
            CaminanteEntity newCaminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(newCaminante);
            CaminataEntity newCaminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(newCaminata);
            caminataCaminanteService.getCaminante(newCaminata.getId(), newCaminante.getId());
        });
    }

    /*
     * Prueba para actualizar los caminantes de una caminata
     */
    @Test
    void testReplaceCaminantes() throws EntityNotFoundException
    {
        List<CaminanteEntity> newCaminantes = new ArrayList<>();
        for(int i = 0; i < 3; i++)
        {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(caminante);
            caminata.getCaminantes().add(caminante);
            newCaminantes.add(caminante);
        }
        caminataCaminanteService.replaceCaminantes(caminata.getId(), newCaminantes);

        List<CaminanteEntity> caminantesEntities = caminataCaminanteService.replaceCaminantes(caminata.getId(), newCaminantes);
       for(CaminanteEntity aNuevaLista: newCaminantes)
       {
           assertTrue(caminantesEntities.contains(aNuevaLista));
       }
    }

    /*
     * Prueba para actualizar los caminantes de una caminata que no existe
     */
    @Test
    void testReplaceCaminantesInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            List<CaminanteEntity> newCaminantes = new ArrayList<>();
            for(int i = 0; i < 3; i++)
            {
                CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
                caminante.getCaminatas().add(caminata);
                entityManager.persist(caminante);
                newCaminantes.add(caminante);
            }
            caminataCaminanteService.replaceCaminantes(0L, newCaminantes);
        });
    }

    /*
     * Prueba para actualizar los caminantes que no existen de una caminata 
     */
    @Test
    void testReplaceInvalidCaminantes()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            List<CaminanteEntity> newCaminantes = new ArrayList<>();
            for(int i = 0; i < 3; i++)
            {
                CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
                caminante.setId(0L);
                newCaminantes.add(caminante);
                caminataCaminanteService.replaceCaminantes(caminata.getId(), newCaminantes);
            }
        });
    }

    /*
     * Prueba para desasociar un caminante de una caminata
     */
    @Test
    void testRemoveCaminante() throws EntityNotFoundException
    {
        for(CaminanteEntity caminante: caminantes)
        {
            caminataCaminanteService.removeCaminante(caminata.getId(), caminante.getId());
        }
        assertTrue(caminataCaminanteService.listCaminantes(caminata.getId()).isEmpty());
    }

    /*
     * Prueba para desasociar un caminante de una caminata que no existe
     */
    @Test
    void testRemoveCaminanteInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminataCaminanteService.removeCaminante(0L, caminantes.get(0).getId());
        });
    }

    /*
     * Prueba para desasociar un caminante que no existe de una caminata
     */
    @Test
    void testRemoveInvalidCaminante()
    {
        assertThrows(EntityNotFoundException.class, () -> 
        {
            caminataCaminanteService.removeCaminante(caminata.getId(), 0L);
        });
    }
}