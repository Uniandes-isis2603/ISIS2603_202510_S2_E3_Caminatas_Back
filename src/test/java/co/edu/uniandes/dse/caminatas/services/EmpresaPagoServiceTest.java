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

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({EmpresaService.class, PagoService.class, EmpresaPagoService.class})
public class EmpresaPagoServiceTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private EmpresaPagoService empresaPagoService;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EmpresaEntity> empresas = new ArrayList<>();
    private List<PagoEntity> pagos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PagoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas
     */
    private void insertData() {
        // Crear y persistir empresas
        for (int i = 0; i < 3; i++) {
            EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
            // Asegurar que los datos cumplen con las reglas de negocio
            empresa.setNombre("Empresa Test " + i);
            empresa.setDocumento("123456789-" + i);
            empresa.setCorreo("empresa" + i + "@test.com");
            entityManager.persist(empresa);
            empresas.add(empresa);
        }
        
        // Crear y persistir pagos
        for (int i = 0; i < 3; i++) {
            PagoEntity pago = factory.manufacturePojo(PagoEntity.class);
            // Asegurar que los datos cumplen con las reglas de negocio
            pago.setNumeroTransaccion("TRANS" + i);
            pago.setValorCaminata(100.0f);  // Usar float en lugar de double
            pago.setValorTotal(120.0f);     // Usar float en lugar de double
            pago.setNumeroTarjeta("1234567890123456");
            pago.setCcv("123");
            // No asociamos empresa ni caminante inicialmente
            pago.setEmpresa(null);
            pago.setCaminante(null);
            // Necesitamos una caminata para el pago
            pago.setCaminata(factory.manufacturePojo(co.edu.uniandes.dse.caminatas.entities.CaminataEntity.class));
            entityManager.persist(pago.getCaminata());
            entityManager.persist(pago);
            pagos.add(pago);
        }
    }

    /**
     * Prueba para asociar un pago a una empresa
     */
    @Test
    public void addPagoToEmpresaTest() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        PagoEntity result = empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId());
        
        // Verificar que el pago fue asociado correctamente
        assertNotNull(result);
        assertEquals(empresa.getId(), result.getEmpresa().getId());
        
        // Verificar que el pago recuperado de la base de datos tiene la empresa asociada
        PagoEntity storedPago = entityManager.find(PagoEntity.class, pago.getId());
        assertNotNull(storedPago.getEmpresa());
        assertEquals(empresa.getId(), storedPago.getEmpresa().getId());
    }

    /**
     * Prueba para asociar un pago a una empresa que no existe
     */
    @Test
    public void addPagoToNonExistentEmpresaTest() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.addPagoToEmpresa(0L, pago.getId()));
    }

    /**
     * Prueba para asociar un pago que no existe a una empresa
     */
    @Test
    public void addNonExistentPagoToEmpresaTest() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.addPagoToEmpresa(empresa.getId(), 0L));
    }

    /**
     * Prueba para asociar un pago que ya está asociado a un caminante
     */
    @Test
    public void addPagoWithCaminanteToEmpresaTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a un caminante
        pago.setCaminante(factory.manufacturePojo(co.edu.uniandes.dse.caminatas.entities.CaminanteEntity.class));
        entityManager.persist(pago.getCaminante());
        entityManager.persist(pago);
        
        assertThrows(IllegalOperationException.class, () -> 
            empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId()));
    }

    /**
     * Prueba para obtener un pago de una empresa
     */
    @Test
    public void getPagoFromEmpresaTest() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a la empresa
        pago.setEmpresa(empresa);
        entityManager.persist(pago);
        
        PagoEntity result = empresaPagoService.getPagoFromEmpresa(empresa.getId(), pago.getId());
        
        assertNotNull(result);
        assertEquals(pago.getId(), result.getId());
        assertEquals(empresa.getId(), result.getEmpresa().getId());
    }

    /**
     * Prueba para obtener un pago de una empresa que no existe
     */
    @Test
    public void getPagoFromNonExistentEmpresaTest() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagoFromEmpresa(0L, pago.getId()));
    }

    /**
     * Prueba para obtener un pago que no existe de una empresa
     */
    @Test
    public void getNonExistentPagoFromEmpresaTest() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagoFromEmpresa(empresa.getId(), 0L));
    }

    /**
     * Prueba para obtener un pago que no está asociado a la empresa
     */
    @Test
    public void getPagoNotAssociatedWithEmpresaTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // El pago no está asociado a ninguna empresa
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagoFromEmpresa(empresa.getId(), pago.getId()));
    }

    /**
     * Prueba para obtener todos los pagos de una empresa
     */
    @Test
    public void getPagosFromEmpresaTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        
        // Asociar dos pagos a la empresa
        PagoEntity pago1 = pagos.get(0);
        PagoEntity pago2 = pagos.get(1);
        
        pago1.setEmpresa(empresa);
        pago2.setEmpresa(empresa);
        
        entityManager.persist(pago1);
        entityManager.persist(pago2);
        
        List<PagoEntity> result = empresaPagoService.getPagosFromEmpresa(empresa.getId());
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(pago1));
        assertTrue(result.contains(pago2));
    }

    /**
     * Prueba para obtener todos los pagos de una empresa que no existe
     */
    @Test
    public void getPagosFromNonExistentEmpresaTest() {
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagosFromEmpresa(0L));
    }

    /**
     * Prueba para obtener todos los pagos de una empresa que no tiene pagos
     */
    @Test
    public void getPagosFromEmpresaWithNoPagosTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        
        List<PagoEntity> result = empresaPagoService.getPagosFromEmpresa(empresa.getId());
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Prueba para desasociar un pago de una empresa
     */
    @Test
    public void removePagoFromEmpresaTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a la empresa
        pago.setEmpresa(empresa);
        entityManager.persist(pago);
        
        empresaPagoService.removePagoFromEmpresa(empresa.getId(), pago.getId());
        
        // Verificar que el pago ya no está asociado a la empresa
        PagoEntity storedPago = entityManager.find(PagoEntity.class, pago.getId());
        assertNotNull(storedPago);
        assertEquals(null, storedPago.getEmpresa());
    }

    /**
     * Prueba para desasociar un pago de una empresa que no existe
     */
    @Test
    public void removePagoFromNonExistentEmpresaTest() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(0L, pago.getId()));
    }

    /**
     * Prueba para desasociar un pago que no existe de una empresa
     */
    @Test
    public void removeNonExistentPagoFromEmpresaTest() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(empresa.getId(), 0L));
    }

    /**
     * Prueba para desasociar un pago que no está asociado a la empresa
     */
    @Test
    public void removePagoNotAssociatedWithEmpresaTest() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // El pago no está asociado a ninguna empresa
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(empresa.getId(), pago.getId()));
    }
}