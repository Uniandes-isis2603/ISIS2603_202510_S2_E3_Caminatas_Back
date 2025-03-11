package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EmpresaService.class)
class EmpresaServiceTest {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EmpresaEntity> empresaList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia la tabla de EmpresaEntity.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
    }

    /**
     * Inserta datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
            empresa.setNombre("Empresa" + i);
            empresa.setDocumento("123456789-" + i);
            empresa.setCorreo("empresa" + i + "@example.com");
            entityManager.persist(empresa);
            empresaList.add(empresa);
        }
    }

    /**
     * Prueba para crear una Empresa con datos válidos.
     */
    @Test
    void testCreateEmpresaValidData() throws IllegalOperationException {
        EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
        empresa.setNombre("Nueva Empresa");
        empresa.setDocumento("987654321-0");
        empresa.setCorreo("nueva.empresa@example.com");

        EmpresaEntity result = empresaService.createEmpresa(empresa);
        assertNotNull(result);

        EmpresaEntity entity = entityManager.find(EmpresaEntity.class, result.getId());
        assertEquals(empresa.getNombre(), entity.getNombre());
        assertEquals(empresa.getDocumento(), entity.getDocumento());
        assertEquals(empresa.getCorreo(), entity.getCorreo());
    }

    /**
     * Prueba para crear una Empresa con nombre inválido.
     */
    @Test
    void testCreateEmpresaInvalidNombre() {
        EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
        empresa.setNombre("");
        empresa.setDocumento("987654321-0");
        empresa.setCorreo("nueva.empresa@example.com");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.createEmpresa(empresa);
        });
        assertTrue(exception.getMessage().contains("El nombre es obligatorio"));

        empresa.setNombre("Empresa123");
        exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.createEmpresa(empresa);
        });
        assertTrue(exception.getMessage().contains("El nombre solo debe contener caracteres alfabéticos"));
    }

    /**
     * Prueba para crear una Empresa con documento inválido.
     */
    @Test
    void testCreateEmpresaInvalidDocumento() {
        EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
        empresa.setNombre("Nueva Empresa");
        empresa.setDocumento("123456789");
        empresa.setCorreo("nueva.empresa@example.com");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.createEmpresa(empresa);
        });
        assertTrue(exception.getMessage().contains("El NIT debe tener el formato válido"));

        empresa.setDocumento("000000000-0");
        exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.createEmpresa(empresa);
        });
        assertTrue(exception.getMessage().contains("El número del NIT no puede ser todo ceros"));
    }

    /**
     * Prueba para crear una Empresa con correo inválido.
     */
    @Test
    void testCreateEmpresaInvalidCorreo() {
        EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
        empresa.setNombre("Nueva Empresa");
        empresa.setDocumento("987654321-0");
        empresa.setCorreo("correo.invalido");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.createEmpresa(empresa);
        });
        assertTrue(exception.getMessage().contains("El correo tiene un formato inválido"));
    }

    /**
     * Prueba para actualizar una Empresa con datos válidos.
     */
    @Test
    void testUpdateEmpresaValidData() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity entity = empresaList.get(0);
        EmpresaEntity empresaAct = factory.manufacturePojo(EmpresaEntity.class);
        empresaAct.setNombre("Empresa Actualizada");
        empresaAct.setDocumento("111222333-4");
        empresaAct.setCorreo("actualizada@example.com");

        EmpresaEntity empresaUpdated = empresaService.updateEmpresa(entity.getId(), empresaAct);
        assertNotNull(empresaUpdated);
        assertEquals("Empresa Actualizada", empresaUpdated.getNombre());
        assertEquals("111222333-4", empresaUpdated.getDocumento());
        assertEquals("actualizada@example.com", empresaUpdated.getCorreo());
    }

    /**
     * Prueba para actualizar una Empresa con nombre inválido.
     */
    @Test
    void testUpdateEmpresaInvalidNombre() {
        EmpresaEntity entity = empresaList.get(0);
        EmpresaEntity empresaAct = factory.manufacturePojo(EmpresaEntity.class);
        empresaAct.setNombre("");
        empresaAct.setDocumento("111222333-4");
        empresaAct.setCorreo("actualizada@example.com");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.updateEmpresa(entity.getId(), empresaAct);
        });
        assertTrue(exception.getMessage().contains("El nombre es obligatorio"));

        empresaAct.setNombre("Empresa123");
        exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.updateEmpresa(entity.getId(), empresaAct);
        });
        assertTrue(exception.getMessage().contains("El nombre solo debe contener caracteres alfabéticos"));
    }

    /**
     * Prueba para actualizar una Empresa con documento inválido.
     */
    @Test
    void testUpdateEmpresaInvalidDocumento() {
        EmpresaEntity entity = empresaList.get(0);
        EmpresaEntity empresaAct = factory.manufacturePojo(EmpresaEntity.class);
        empresaAct.setNombre("Empresa Actualizada");
        empresaAct.setDocumento("123456789");
        empresaAct.setCorreo("actualizada@example.com");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.updateEmpresa(entity.getId(), empresaAct);
        });
        assertTrue(exception.getMessage().contains("El NIT debe tener el formato válido"));

        empresaAct.setDocumento("000000000-0");
        exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.updateEmpresa(entity.getId(), empresaAct);
        });
        assertTrue(exception.getMessage().contains("El número del NIT no puede ser todo ceros"));
    }

    /**
     * Prueba para actualizar una Empresa con correo inválido.
     */
    @Test
    void testUpdateEmpresaInvalidCorreo() {
        EmpresaEntity entity = empresaList.get(0);
        EmpresaEntity empresaAct = factory.manufacturePojo(EmpresaEntity.class);
        empresaAct.setNombre("Empresa Actualizada");
        empresaAct.setDocumento("111222333-4");
        empresaAct.setCorreo("correo.invalido");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            empresaService.updateEmpresa(entity.getId(), empresaAct);
        });
        assertTrue(exception.getMessage().contains("El correo tiene un formato inválido"));
    }

    /**
     * Prueba para obtener la lista de Empresas.
     */
    @Test
    void testGetEmpresas() {
        List<EmpresaEntity> list = empresaService.getEmpresas();
        assertEquals(empresaList.size(), list.size());
        for (EmpresaEntity entity : list) {
            boolean found = empresaList.stream()
                              .anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    /**
     * Prueba para consultar una Empresa existente.
     */
    @Test
    void testGetEmpresa() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        EmpresaEntity result = empresaService.getEmpresa(empresa.getId());
        assertNotNull(result);
        assertEquals(empresa.getNombre(), result.getNombre());
        assertEquals(empresa.getDocumento(), result.getDocumento());
        assertEquals(empresa.getCorreo(), result.getCorreo());
    }

    /**
     * Prueba para consultar una Empresa que no existe.
     */
    @Test
    void testGetInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            empresaService.getEmpresa(0L);
        });
    }

    /**
     * Prueba para eliminar una Empresa.
     */
    @Test
    void testDeleteEmpresa() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        empresaService.deleteEmpresa(empresa.getId());
        EmpresaEntity deleted = entityManager.find(EmpresaEntity.class, empresa.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar una Empresa que no existe.
     */
    @Test
    void testDeleteInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            empresaService.deleteEmpresa(0L);
        });
    }
}

