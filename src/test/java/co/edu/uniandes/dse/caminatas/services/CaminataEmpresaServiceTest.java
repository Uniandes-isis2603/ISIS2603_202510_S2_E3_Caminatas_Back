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

    @Test
    public void addEmpresaTest() throws EntityNotFoundException
    {
        CaminataEntity caminata = caminatas.get(0);
        EmpresaEntity empresa = empresas.get(0);
        caminataEmpresaService.addEmpresa(caminata.getId(), empresa.getId());
        CaminataEntity caminataBuscada = caminataService.getCaminata(caminata.getId());
        assertEquals(empresa, caminataBuscada.getEmpresa());
    }

    @Test
    public void addEmpresaNoCaminataTest() throws EntityNotFoundException
    {
        EmpresaEntity empresa = empresas.get(0);
        assertThrows(EntityNotFoundException.class, () -> caminataEmpresaService.addEmpresa(0L, empresa.getId()));
    }

    @Test
    public void addEmpresaNoEmpresaTest() throws EntityNotFoundException
    {
        CaminataEntity caminata = caminatas.get(0);
        assertThrows(EntityNotFoundException.class, () -> caminataEmpresaService.addEmpresa(caminata.getId(), 0L));
    }

    @Test
    public void getEmpresaTest() throws EntityNotFoundException
    {
        CaminataEntity caminata = caminatas.get(0);
        EmpresaEntity empresa = empresas.get(0);
        caminata.setEmpresa(empresa);
        entityManager.persist(caminata);
        EmpresaEntity empresaBuscada = caminataEmpresaService.getEmpresa(caminata.getId(), empresa.getId());
        assertEquals(empresa, empresaBuscada);
    }

    @Test
    public void getEmpresaNoCaminataTest() throws EntityNotFoundException
    {
        EmpresaEntity empresa = empresas.get(0);
        assertThrows(EntityNotFoundException.class, () -> caminataEmpresaService.getEmpresa(0L, empresa.getId()));
    }

    @Test
    public void getNoEmpresaCaminataTest() throws EntityNotFoundException
    {
        CaminataEntity caminata = caminatas.get(0);
        assertNull(caminataEmpresaService.getEmpresa(caminata.getId(), 0L));
    }
    
}
