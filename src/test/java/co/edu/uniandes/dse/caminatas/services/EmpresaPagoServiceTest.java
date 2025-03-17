package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
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
    private List<CaminataEntity> caminatas = new ArrayList<>();
    private List<CaminanteEntity> caminantes = new ArrayList<>();
    
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
        entityManager.getEntityManager().createQuery("delete from CaminanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
    }
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        // Crear caminatas
        for (int i = 0; i < 3; i++) {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata);
            caminatas.add(caminata);
        }
        
        // Crear empresas
        for (int i = 0; i < 3; i++) {
            EmpresaEntity empresa = factory.manufacturePojo(EmpresaEntity.class);
            empresa.setDocumento("123456789-" + i);
            empresa.setNombre("Empresa " + i);
            empresa.setCorreo("empresa" + i + "@test.com");
            entityManager.persist(empresa);
            empresas.add(empresa);
        }
        
        // Crear caminantes
        for (int i = 0; i < 3; i++) {
            CaminanteEntity caminante = factory.manufacturePojo(CaminanteEntity.class);
            entityManager.persist(caminante);
            caminantes.add(caminante);
        }
        
        // Crear pagos (sin asociar a empresas o caminantes)
        for (int i = 0; i < 6; i++) {
            PagoEntity pago = factory.manufacturePojo(PagoEntity.class);
            pago.setNumeroTransaccion("TRANS" + i); // Transacciones únicas
            pago.setNumeroTarjeta("1234567890123456"); // 16 dígitos
            pago.setCcv("123"); // 3 dígitos
            
            pago.setValorCaminata(Float.valueOf(100.0f));
            pago.setValorTotal(Float.valueOf(100.0f));
            
            pago.setCaminata(caminatas.get(i % 3)); // Asignar caminata
            pago.setEmpresa(null);
            pago.setCaminante(null);
            entityManager.persist(pago);
            pagos.add(pago);
        }
    }
    
    /**
     * Prueba para asociar un pago a una empresa.
     */
    @Test
    void testAddPagoToEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        PagoEntity result = empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId());
        
        assertNotNull(result);
        assertEquals(empresa.getId(), result.getEmpresa().getId());
    }
    
    /**
     * Prueba para asociar un pago a una empresa que no existe.
     */
    @Test
    void testAddPagoToNonExistentEmpresa() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.addPagoToEmpresa(0L, pago.getId())
        );
    }
    
    /**
     * Prueba para asociar un pago que no existe a una empresa.
     */
    @Test
    void testAddNonExistentPagoToEmpresa() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.addPagoToEmpresa(empresa.getId(), 0L)
        );
    }
    
    /**
     * Prueba para asociar un pago que ya está asociado a un caminante.
     */
    @Test
    void testAddPagoWithCaminanteToEmpresa() {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a un caminante
        pago.setCaminante(caminantes.get(0));
        entityManager.persist(pago);
        
        assertThrows(IllegalOperationException.class, () -> 
            empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId())
        );
    }
    
    /**
     * Prueba para obtener los pagos de una empresa.
     */
    @Test
    void testGetPagosEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        
        // Asociar dos pagos a la empresa
        PagoEntity pago1 = pagos.get(0);
        PagoEntity pago2 = pagos.get(1);
        
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pago1.getId());
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pago2.getId());
        
        List<PagoEntity> result = empresaPagoService.getPagosEmpresa(empresa.getId());
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    /**
     * Prueba para obtener los pagos de una empresa que no existe.
     */
    @Test
    void testGetPagosNonExistentEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagosEmpresa(0L)
        );
    }
    
    /**
     * Prueba para obtener un pago específico de una empresa.
     */
    @Test
    void testGetPagoEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId());
        
        PagoEntity result = empresaPagoService.getPagoEmpresa(empresa.getId(), pago.getId());
        
        assertNotNull(result);
        assertEquals(pago.getId(), result.getId());
    }
    
    /**
     * Prueba para obtener un pago específico de una empresa que no existe.
     */
    @Test
    void testGetPagoNonExistentEmpresa() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagoEmpresa(0L, pago.getId())
        );
    }
    
    /**
     * Prueba para obtener un pago que no existe de una empresa.
     */
    @Test
    void testGetNonExistentPagoEmpresa() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.getPagoEmpresa(empresa.getId(), 0L)
        );
    }
    
    /**
     * Prueba para obtener un pago que no está asociado a la empresa.
     */
    @Test
    void testGetPagoNotAssociatedWithEmpresa() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // El pago no está asociado a ninguna empresa
        assertNull(pago.getEmpresa());
        
        assertThrows(IllegalOperationException.class, () -> 
            empresaPagoService.getPagoEmpresa(empresa.getId(), pago.getId())
        );
    }
    
    /**
     * Prueba para reemplazar los pagos de una empresa.
     */
    @Test
    void testReplacePagosEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        
        // Asociar dos pagos inicialmente
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pagos.get(0).getId());
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pagos.get(1).getId());
        
        // Lista de pagos para reemplazar
        List<PagoEntity> nuevaListaPagos = new ArrayList<>();
        nuevaListaPagos.add(pagos.get(2));
        nuevaListaPagos.add(pagos.get(3));
        
        List<PagoEntity> result = empresaPagoService.replacePagosEmpresa(empresa.getId(), nuevaListaPagos);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verificar que los pagos anteriores ya no estén asociados
        entityManager.refresh(pagos.get(0));
        entityManager.refresh(pagos.get(1));
        assertNull(pagos.get(0).getEmpresa());
        assertNull(pagos.get(1).getEmpresa());
        
        // Verificar que los nuevos pagos estén asociados
        entityManager.refresh(pagos.get(2));
        entityManager.refresh(pagos.get(3));
        assertEquals(empresa.getId(), pagos.get(2).getEmpresa().getId());
        assertEquals(empresa.getId(), pagos.get(3).getEmpresa().getId());
    }
    
    /**
     * Prueba para reemplazar los pagos de una empresa que no existe.
     */
    @Test
    void testReplacePagosNonExistentEmpresa() {
        List<PagoEntity> nuevaListaPagos = new ArrayList<>();
        nuevaListaPagos.add(pagos.get(0));
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.replacePagosEmpresa(0L, nuevaListaPagos)
        );
    }
    
    /**
     * Prueba para reemplazar con pagos que no existen.
     */
    @Test
    void testReplacePagosWithNonExistentPago() {
        EmpresaEntity empresa = empresas.get(0);
        
        // Crear un pago que no existe en la base de datos
        PagoEntity pagoNoExistente = factory.manufacturePojo(PagoEntity.class);
        pagoNoExistente.setId(999L);
        
        List<PagoEntity> nuevaListaPagos = new ArrayList<>();
        nuevaListaPagos.add(pagoNoExistente);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.replacePagosEmpresa(empresa.getId(), nuevaListaPagos)
        );
    }
    
    /**
     * Prueba para reemplazar con un pago que ya está asociado a un caminante.
     */
    @Test
    void testReplacePagosWithPagoAssociatedToCaminante() {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a un caminante
        pago.setCaminante(caminantes.get(0));
        entityManager.persist(pago);
        
        List<PagoEntity> nuevaListaPagos = new ArrayList<>();
        nuevaListaPagos.add(pago);
        
        assertThrows(IllegalOperationException.class, () -> 
            empresaPagoService.replacePagosEmpresa(empresa.getId(), nuevaListaPagos)
        );
    }
    
    /**
     * Prueba para remover un pago de una empresa.
     */
    @Test
    void testRemovePagoFromEmpresa() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // Asociar el pago a la empresa
        empresaPagoService.addPagoToEmpresa(empresa.getId(), pago.getId());
        
        // Verificar que el pago está asociado
        entityManager.refresh(pago);
        assertNotNull(pago.getEmpresa());
        
        // Remover la asociación
        empresaPagoService.removePagoFromEmpresa(empresa.getId(), pago.getId());
        
        // Verificar que el pago ya no está asociado
        entityManager.refresh(pago);
        assertNull(pago.getEmpresa());
    }

    /**
     * Prueba para remover un pago de una empresa que no existe.
     */
    @Test
    void testRemovePagoFromNonExistentEmpresa() {
        PagoEntity pago = pagos.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(0L, pago.getId())
        );
    }

    /**
     * Prueba para remover un pago que no existe de una empresa.
     */
    @Test
    void testRemoveNonExistentPagoFromEmpresa() {
        EmpresaEntity empresa = empresas.get(0);
        
        assertThrows(EntityNotFoundException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(empresa.getId(), 0L)
        );
    }

    /**
     * Prueba para remover un pago que no está asociado a la empresa.
     */
    @Test
    void testRemovePagoNotAssociatedWithEmpresa() throws EntityNotFoundException {
        EmpresaEntity empresa = empresas.get(0);
        PagoEntity pago = pagos.get(0);
        
        // El pago no está asociado a ninguna empresa
        assertNull(pago.getEmpresa());
        
        assertThrows(IllegalOperationException.class, () -> 
            empresaPagoService.removePagoFromEmpresa(empresa.getId(), pago.getId())
        );
    }
}