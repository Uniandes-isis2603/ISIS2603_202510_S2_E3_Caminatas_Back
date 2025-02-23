package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DataJpaTest
@Transactional
@Import(EmpresaPagoService.class)
class EmpresaPagoServiceTest {

    @Autowired
    private EmpresaPagoService empresaPagoService;

    @Autowired
    private TestEntityManager entityManager;

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
            EmpresaEntity empresa = new EmpresaEntity();
            empresa.setNombre("Empresa " + i);
            empresa.setDocumento(123456789 + i);
            empresa.setCorreo("empresa" + i + "@example.com");
            entityManager.persist(empresa);
            empresaList.add(empresa);
        }

        for (int i = 0; i < 3; i++) {
            PagoEntity pago = new PagoEntity();
            pago.setNumeroTransaccion("TRANS" + i);
            pago.setValorCaminata(100.0f * (i + 1));
            pago.setValorTotal(120.0f * (i + 1));
            pago.setFechaVencimiento(new Date());
            pago.setNumeroTarjeta("1234567890123456");
            pago.setCcv("123");
            pago.setCuotas(i + 1);
            pago.setPropietario("Propietario " + i);
            entityManager.persist(pago);
            pagoList.add(pago);
        }
    }

    @Test
    void testAddPago() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);

        PagoEntity result = empresaPagoService.addPago(empresa.getId(), pago.getId());
        assertNotNull(result);
        assertEquals(empresa.getId(), result.getEmpresa().getId());
        assertTrue(empresa.getPagos().contains(result));
    }

    @Test
    void testGetPagos() throws EntityNotFoundException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);
        empresaPagoService.addPago(empresa.getId(), pago.getId());

        List<PagoEntity> pagos = empresaPagoService.getPagos(empresa.getId());
        assertEquals(1, pagos.size());
        assertEquals(pago.getId(), pagos.get(0).getId());
    }

    @Test
    void testGetPago() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);
        empresaPagoService.addPago(empresa.getId(), pago.getId());

        PagoEntity result = empresaPagoService.getPago(empresa.getId(), pago.getId());
        assertNotNull(result);
        assertEquals(pago.getId(), result.getId());
    }

    @Test
    void testRemovePago() throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresa = empresaList.get(0);
        PagoEntity pago = pagoList.get(0);
        empresaPagoService.addPago(empresa.getId(), pago.getId());

        empresaPagoService.removePago(empresa.getId(), pago.getId());
        List<PagoEntity> pagos = empresaPagoService.getPagos(empresa.getId());
        assertTrue(pagos.isEmpty());
    }

    @Test
    void testAddInvalidPago() {
        assertThrows(EntityNotFoundException.class, () -> {
            empresaPagoService.addPago(0L, 0L);
        });
    }

    @Test
    void testGetInvalidPago() {
        assertThrows(EntityNotFoundException.class, () -> {
            empresaPagoService.getPago(0L, 0L);
        });
    }
}