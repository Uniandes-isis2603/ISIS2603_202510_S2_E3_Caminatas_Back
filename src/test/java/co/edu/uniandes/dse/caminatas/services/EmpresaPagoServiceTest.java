package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EmpresaPagoService.class)
class EmpresaPagoServiceTest {

    @Autowired
    private EmpresaPagoService empresaPagoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EmpresaEntity> empresaList = new ArrayList<>();
    private List<PagoEntity> pagoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PagoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EmpresaEntity empresaEntity = factory.manufacturePojo(EmpresaEntity.class);
            entityManager.persist(empresaEntity);
            empresaList.add(empresaEntity);
        }

        for (int i = 0; i < 3; i++) {
            PagoEntity pagoEntity = factory.manufacturePojo(PagoEntity.class);
            entityManager.persist(pagoEntity);
            pagoList.add(pagoEntity);
        }

        // Associate initial pagos with the first empresa
        empresaList.get(0).setPagos(new ArrayList<>(pagoList));
    }

    @Test
    void testAddPago() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(1);
        PagoEntity newPago = factory.manufacturePojo(PagoEntity.class);
        entityManager.persist(newPago);

        EmpresaEntity result = empresaPagoService.addPago(empresa.getId(), newPago.getId());

        assertNotNull(result);
        assertTrue(result.getPagos().contains(newPago));
    }

    @Test
    void testAddPagoInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            PagoEntity newPago = factory.manufacturePojo(PagoEntity.class);
            entityManager.persist(newPago);
            empresaPagoService.addPago(0L, newPago.getId());
        });
    }

    @Test
    void testAddPagoInvalidPago() {
        assertThrows(EntityNotFoundException.class, () -> {
            EmpresaEntity empresa = empresaList.get(0);
            empresaPagoService.addPago(empresa.getId(), 0L);
        });
    }

    @Test
    void testGetPagos() throws EntityNotFoundException {
        List<PagoEntity> pagos = empresaPagoService.getPagos(empresaList.get(0).getId());
        assertEquals(pagoList.size(), pagos.size());

        for (PagoEntity pago : pagos) {
            assertTrue(pagoList.contains(pago));
        }
    }

    @Test
    void testGetPagosInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            empresaPagoService.getPagos(0L);
        });
    }

    @Test
    void testGetPago() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);
        PagoEntity result = empresaPagoService.getPago(empresa.getId(), pago.getId());
        assertNotNull(result);
        assertEquals(pago.getId(), result.getId());
    }

    @Test
    void testGetPagoInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            PagoEntity pago = pagoList.get(0);
            empresaPagoService.getPago(0L, pago.getId());
        });
    }

    @Test
    void testGetPagoInvalidPago() {
        assertThrows(EntityNotFoundException.class, () -> {
            EmpresaEntity empresa = empresaList.get(0);
            empresaPagoService.getPago(empresa.getId(), 0L);
        });
    }

    @Test
    void testReplacePagos() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        List<PagoEntity> newPagos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            PagoEntity pago = factory.manufacturePojo(PagoEntity.class);
            entityManager.persist(pago);
            newPagos.add(pago);
        }

        List<PagoEntity> result = empresaPagoService.replacePagos(empresa.getId(), newPagos);

        assertNotNull(result);
        assertEquals(newPagos.size(), result.size());

        for (PagoEntity pago : newPagos) {
            assertTrue(result.contains(pago));
        }

        // Verify that old pagos are not present
        for (PagoEntity oldPago : pagoList) {
            assertFalse(result.contains(oldPago));
        }
    }

    @Test
    void testReplacePagosInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<PagoEntity> newPagos = new ArrayList<>();
            empresaPagoService.replacePagos(0L, newPagos);
        });
    }

    @Test
    void testRemovePago() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);

        empresaPagoService.removePago(empresa.getId(), pago.getId());

        EmpresaEntity updatedEmpresa = entityManager.find(EmpresaEntity.class, empresa.getId());
        assertFalse(updatedEmpresa.getPagos().contains(pago));
    }

    @Test
    void testRemovePagoInvalidEmpresa() {
        assertThrows(EntityNotFoundException.class, () -> {
            PagoEntity pago = pagoList.get(0);
            empresaPagoService.removePago(0L, pago.getId());
        });
    }

    @Test
    void testRemovePagoInvalidPago() {
        assertThrows(EntityNotFoundException.class, () -> {
            EmpresaEntity empresa = empresaList.get(0);
            empresaPagoService.removePago(empresa.getId(), 0L);
        });
    }
}