package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CaminataService.class, CaminataEmpresaService.class})

public class CaminataEmpresaServiceTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private CaminataEmpresaService caminataEmpresaService;

    @Autowired
    private CaminataService caminataService;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminataEntity> caminatas = new ArrayList<>();
    private List<EmpresaEntity> empresas = new ArrayList<>();

    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }

    /*
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData(){
        entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
    }

    /*
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas
     */
    private void insertData()
    {
        for(int i = 0; i < 3; i++)
        {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata); // Persistimos la entidad individualmente
            caminatas.add(caminata); // Luego la añadimos a la lista
        }
    
        for(int i = 0; i < 3; i++)
        {
            EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
            entityManager.persist(empresa); // Persistimos la entidad individualmente
            empresas.add(empresa); // Luego la añadimos a la lista
        }
    }

    /*
     * Prueba para reemplazar las instancias de Caminatas asociadas a una instancia de Empresa
     */
    @Test
    void testReplaceEmpresa() throws EntityNotFoundException {
        CaminataEntity caminata = caminatas.get(0);
        caminataEmpresaService.replaceEmpresa(caminata.getId(), empresas.get(1).getId());
        caminata = caminataService.getCaminata(caminata.getId());
        assertEquals(caminata.getEmpresa(), empresas.get(1));
    }

    /*
     * Prueba para reemplazar las instancias de Caminatas asociadas a una instancia de Empresa con una caminata que no existe.
     */
    @Test
    void testReplaceEmpresaInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, ()->{
            caminataEmpresaService.replaceEmpresa(0L, empresas.get(1).getId());
        });
    }

    /*
     * Prueba para reemplazar las instancias de Caminatas a una instancia de Empresa que no existe. 
     */
    @Test
    void testReplaceInvalidEmpresa()
    {
        assertThrows(EntityNotFoundException.class, ()->{
            EmpresaEntity empresa = empresas.get(0);
            caminataEmpresaService.replaceEmpresa(empresa.getId(), 0L);
        });
    }

    /*
     * Prueba para desasociar una caminata de una empresa existente
     */
    @Test
    void testRemoveEmpresa() throws EntityNotFoundException 
    {
        caminataEmpresaService.removeEmpresa(caminatas.get(0).getId());
        CaminataEntity caminata = caminataService.getCaminata(caminatas.get(0).getId());
        assertNull(caminata.getEmpresa());
    }

    /*
     * Prueba para desasociar una caminata que no existe de una empresa existente
     */
    @Test
    void testRemoveEmpresaInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, ()->{
            caminataEmpresaService.removeEmpresa(0L);
        });
    }  
    
}
