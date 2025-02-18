package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(EmpresaService.class)
class EmpresaServiceTest {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private TestEntityManager entityManager;

    private EmpresaEntity testEmpresa;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
    }

    private void insertData() {
        testEmpresa = new EmpresaEntity();
        testEmpresa.setNombre("Test Empresa");
        testEmpresa.setDocumento(12345);
        testEmpresa.setCorreo("test@empresa.com");
        entityManager.persist(testEmpresa);
    }

    @Test
    void testCreateEmpresa() throws IllegalOperationException {
        EmpresaEntity newEntity = new EmpresaEntity();
        newEntity.setNombre("Nueva Empresa");
        newEntity.setDocumento(67890);
        newEntity.setCorreo("nueva@empresa.com");
        
        EmpresaEntity result = empresaService.createEmpresa(newEntity);
        assertNotNull(result);
        assertEquals(newEntity.getNombre(), result.getNombre());
        assertEquals(newEntity.getDocumento(), result.getDocumento());
        assertEquals(newEntity.getCorreo(), result.getCorreo());
    }

    @Test
    void testGetEmpresa() throws EntityNotFoundException {
        EmpresaEntity result = empresaService.getEmpresa(testEmpresa.getId());
        assertNotNull(result);
        assertEquals(testEmpresa.getId(), result.getId());
        assertEquals(testEmpresa.getNombre(), result.getNombre());
    }

    @Test
    void testGetEmpresas() {
        List<EmpresaEntity> list = empresaService.getEmpresas();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity updatedEmpresa = new EmpresaEntity();
        updatedEmpresa.setNombre("Updated Empresa");
        updatedEmpresa.setDocumento(54321);
        updatedEmpresa.setCorreo("updated@empresa.com");
        
        EmpresaEntity result = empresaService.updateEmpresa(testEmpresa.getId(), updatedEmpresa);
        assertNotNull(result);
        assertEquals(updatedEmpresa.getNombre(), result.getNombre());
        assertEquals(updatedEmpresa.getDocumento(), result.getDocumento());
        assertEquals(updatedEmpresa.getCorreo(), result.getCorreo());
    }

    @Test
    void testDeleteEmpresa() throws EntityNotFoundException {
        empresaService.deleteEmpresa(testEmpresa.getId());
        EmpresaEntity deleted = entityManager.find(EmpresaEntity.class, testEmpresa.getId());
        assertNull(deleted);
    }
}

