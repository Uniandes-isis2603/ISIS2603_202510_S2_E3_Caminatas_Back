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

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CaminanteService.class)
class CaminanteServiceTest {

    @Autowired
    private CaminanteService caminanteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminanteEntity> caminanteList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia la tabla de CaminanteEntity.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CaminanteEntity").executeUpdate();
    }

    /**
     * Inserta datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            caminante.setNombre("Pepito" + i);
            caminante.setDocumento("1234567" + i);
            caminante.setCorreo("correo" + i + "@gmail.com");
            caminante.setTelefono("123456789" + i);
            caminante.setDireccion("Cra 30 # " + i);
            entityManager.persist(caminante);
            caminanteList.add(caminante);
        }
    }

    /**
     * Prueba para crear un Caminante con datos válidos.
     */
    @Test
    void testCreateCaminanteValidData() throws IllegalOperationException {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        caminante.setNombre("Juan Perez");
        caminante.setDocumento("87654321");
        caminante.setCorreo("juan.perez@gmail.com");
        caminante.setTelefono("3208993490");
        caminante.setDireccion("Cra 54 #8-43");

        CaminanteEntity result = caminanteService.createCaminante(caminante);
        assertNotNull(result);

        CaminanteEntity entity = entityManager.find(CaminanteEntity.class, result.getId());
        assertEquals(caminante.getNombre(), entity.getNombre());
        assertEquals(caminante.getDocumento(), entity.getDocumento());
        assertEquals(caminante.getCorreo(), entity.getCorreo());
        assertEquals(caminante.getTelefono(), entity.getTelefono());
        assertEquals(caminante.getDireccion(), entity.getDireccion());
    }

    /**
    * Prueba para createCaminante con datos inválidos.
    */

    @Test
    void testCreateCaminanteInvalidNombre() {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        // Prubeba con nombre vacío
        caminante.setNombre(" ");
        caminante.setDocumento("87654321");
        caminante.setCorreo("juan.perez@gmail.com");
        caminante.setTelefono("3208993490");
        caminante.setDireccion("Cra 54 #8-43");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El nombre es obligatorio"));

        // Prueba de nombre con números
        caminante.setNombre("Juan123");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El nombre solo debe contener caracteres alfabéticos"));
    }

    @Test
    void testCreateCaminanteInvalidDocumento() {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con documento vacío
        caminante.setDocumento(" ");
        caminante.setNombre("Ana Lopez");
        caminante.setCorreo("ana.lopez@gmail.com");
        caminante.setTelefono("3145678901");
        caminante.setDireccion("Calle 123");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El documento es obligatorio"));

        // Prueba con documento con menos de 8 dígitos
        caminante.setDocumento("1234567");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El documento debe contener solo números y tener al menos 8 dígitos."));
    }

    @Test
    void testCreateCaminanteInvalidCorreo() {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con correo vacío
        caminante.setCorreo(" ");
        caminante.setNombre("Luis Torres");
        caminante.setDocumento("98765432");
        caminante.setTelefono("0987654321");
        caminante.setDireccion("Calle 1");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El correo es obligatorio"));

        // Prueba con correo con formato inválido
        caminante.setCorreo("luis.torres-arroba-gmail.com");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El correo tiene un formato inválido"));
    }

    @Test
    void testCreateCaminanteInvalidTelefono() {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con teléfono vacío
        caminante.setTelefono(" ");
        caminante.setNombre("María Ruiz");
        caminante.setDocumento("87654322");
        caminante.setCorreo("maria.ruiz@gmail.com");
        caminante.setDireccion("Calle 2");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El teléfono es obligatorio"));

        // Prueba con teléfono con menos de 10 dígitos o con caracteres no numéricos
        caminante.setTelefono("12345abcde");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("El teléfono debe contener solo números y tener una longitud de 10 dígitos"));
    }

    @Test
    void testCreateCaminanteInvalidDireccion() {
        CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con dirección vacía
        caminante.setDireccion(" ");
        caminante.setNombre("Carlos Gomez");
        caminante.setDocumento("87654323");
        caminante.setCorreo("carlos.gomez@gmail.com");
        caminante.setTelefono("0123456789");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.createCaminante(caminante);
        });
        assertTrue(exception.getMessage().contains("La dirección es obligatoria"));
    }

    /**
     * Prueba para actualizar un Caminante con datos válidos.
     */
    @Test
    void testUpdateCaminanteValidData() throws EntityNotFoundException, IllegalOperationException {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        caminanteAct.setNombre("Nombre Actualizado");
        caminanteAct.setDocumento("11223344");
        caminanteAct.setCorreo("actualizado@gmail.com");
        caminanteAct.setTelefono("0123456789");
        caminanteAct.setDireccion("Nueva Direccion");

        CaminanteEntity caminanteUpdated = caminanteService.updateCaminante(entity.getId(), caminanteAct);
        assertNotNull(caminanteUpdated);
        assertEquals("Nombre Actualizado", caminanteUpdated.getNombre());
        assertEquals("11223344", caminanteUpdated.getDocumento());
        assertEquals("actualizado@gmail.com", caminanteUpdated.getCorreo());
        assertEquals("0123456789", caminanteUpdated.getTelefono());
        assertEquals("Nueva Direccion", caminanteUpdated.getDireccion());
    }

    /**
     * Prueba para updateCaminante con datos inválidos.
     */

    @Test
    void testUpdateCaminanteInvalidNombre() {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con nombre vacío
        caminanteAct.setNombre(" ");
        caminanteAct.setDocumento("11223344");
        caminanteAct.setCorreo("actualizado@gmail.com");
        caminanteAct.setTelefono("0123456789");
        caminanteAct.setDireccion("Nueva Dirección 2");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El nombre es obligatorio"));

        // Prueba con nombre con caracteres no permitidos
        caminanteAct.setNombre("Actualizado123");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El nombre solo debe contener caracteres alfabéticos"));
    }

    @Test
    void testUpdateCaminanteInvalidDocumento() {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con documento vacío
        caminanteAct.setDocumento(" ");
        caminanteAct.setNombre("Actualizado Nombre");
        caminanteAct.setCorreo("actualizado@gmail.com");
        caminanteAct.setTelefono("0123456789");
        caminanteAct.setDireccion("Nueva Dirección 3");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El documento es obligatorio"));

        // Prueba con documento con menos de 8 dígitos
        caminanteAct.setDocumento("1234567");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El documento debe contener solo números y tener al menos 8 dígitos."));
    }

    @Test
    void testUpdateCaminanteInvalidCorreo() {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con correo vacío
        caminanteAct.setCorreo(" ");
        caminanteAct.setNombre("Actualizado Nombre");
        caminanteAct.setDocumento("11223344");
        caminanteAct.setTelefono("0123456789");
        caminanteAct.setDireccion("Nueva Dirección 4");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El correo es obligatorio"));

        // Prueba con correo con formato inválido
        caminanteAct.setCorreo("correoInvalido.com");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El correo tiene un formato inválido"));
    }

    @Test
    void testUpdateCaminanteInvalidTelefono() {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con teléfono vacío
        caminanteAct.setTelefono(" ");
        caminanteAct.setNombre("Actualizado Nombre");
        caminanteAct.setDocumento("11223344");
        caminanteAct.setCorreo("actualizado@gmail.com");
        caminanteAct.setDireccion("Nueva Dirección 5");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El teléfono es obligatorio"));

        // Prueba con teléfono con formato inválido
        caminanteAct.setTelefono("12345abcde");
        exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("El teléfono debe contener solo números y tener una longitud de 10 dígitos"));
    }

    @Test
    void testUpdateCaminanteInvalidDireccion() {
        CaminanteEntity entity = caminanteList.get(0);
        CaminanteEntity caminanteAct = factory.manufacturePojo(CaminanteEntity.class);
        // Prueba con dirección vacía
        caminanteAct.setDireccion(" ");
        caminanteAct.setNombre("Actualizado Nombre");
        caminanteAct.setDocumento("11223344");
        caminanteAct.setCorreo("actualizado@gmail.com");
        caminanteAct.setTelefono("0123456789");

        Exception exception = assertThrows(IllegalOperationException.class, () -> {
            caminanteService.updateCaminante(entity.getId(), caminanteAct);
        });
        assertTrue(exception.getMessage().contains("La dirección es obligatoria"));
    }

    /**
     * Prueba para obtener la lista de Caminantes.
     */
    @Test
    void testGetCaminantes() {
        List<CaminanteEntity> list = caminanteService.getCaminantes();
        assertEquals(caminanteList.size(), list.size());
        for (CaminanteEntity entity : list) {
            boolean found = caminanteList.stream()
                              .anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Caminante existente.
     */
    @Test
    void testGetCaminante() throws EntityNotFoundException {
        CaminanteEntity caminante = caminanteList.get(0);
        CaminanteEntity result = caminanteService.getCaminante(caminante.getId());
        assertNotNull(result);
        assertEquals(caminante.getNombre(), result.getNombre());
        assertEquals(caminante.getDocumento(), result.getDocumento());
        assertEquals(caminante.getCorreo(), result.getCorreo());
        assertEquals(caminante.getTelefono(), result.getTelefono());
        assertEquals(caminante.getDireccion(), result.getDireccion());
    }

    /**
     * Prueba para consultar un Caminante que no existe.
     */
    @Test
    void testGetInvalidCaminante() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteService.getCaminante(0L);
        });
    }

    /**
     * Prueba para eliminar un Caminante.
     */
    @Test
    void testDeleteCaminante() throws EntityNotFoundException {
        CaminanteEntity caminante = caminanteList.get(0);
        caminanteService.deleteCaminante(caminante.getId());
        CaminanteEntity deleted = entityManager.find(CaminanteEntity.class, caminante.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un Caminante que no existe.
     */
    @Test
    void testDeleteInvalidCaminante() {
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteService.deleteCaminante(0L);
        });
    }
}
