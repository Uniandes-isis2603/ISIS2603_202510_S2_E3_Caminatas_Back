package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CaminataPagoService.class})
public class CaminataPagoServiceTest 
{
    @Autowired
    private CaminataPagoService caminataPagoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminataEntity> listaCaminatas = new ArrayList<>();
    private List<PagoEntity> listaPagos = new ArrayList<>();

    @BeforeEach
    void setUp()
    {
        clearData();
        insertData();
    }

    /*
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData()
    {
        entityManager.getEntityManager().createQuery("delete from PagoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
    }

    /*
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData()
    {
        for (int i = 0; i < 3; i++)
        {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminata);
            listaCaminatas.add(caminata);
        
            PagoEntity pago = factory.manufacturePojo(PagoEntity.class);
            pago.setCaminata(caminata);
            entityManager.persist(pago);
            caminata.getPagos().add(pago);
            listaPagos.add(pago);
        }
    }

    /*
     * Prueba para asociar un Pago a una Caminata.
     */
    @Test
    void testAddPago() throws EntityNotFoundException
    {
        CaminataEntity caminata = listaCaminatas.get(0);
        PagoEntity pago = listaPagos.get(1);
        PagoEntity respuesta = caminataPagoService.addPago(caminata.getId(), pago.getId());
        assertNotNull(respuesta);
        assertEquals(pago.getId(), respuesta.getId());

    }

    /*
     * Prueba para asociar un Pago que no existe a una Caminata
     */
    @Test
    void testAddInvalidPago()
    {
        assertThrows(EntityNotFoundException.class, () ->{
            CaminataEntity entity = listaCaminatas.get(0);
            caminataPagoService.addPago(0L, entity.getId());
        });
    }

    /*
     * Prueba para asociar un Pago a una Caminata que no existe
     */
    @Test
    void testAddPagoInvalidCaminata()
    {
        assertThrows(EntityNotFoundException.class, () ->{
            PagoEntity entity = listaPagos.get(0);
            caminataPagoService.addPago(entity.getId(), 0L);
        });
    }

    /*
     * Prueba para obtener una colección de instancias de pagos asociadas a una instancia Caminata
     */
    @Test
    void testGetPagos() throws EntityNotFoundException
    {
        CaminataEntity caminata = listaCaminatas.get(0);
        List<PagoEntity> pagos = caminataPagoService.listPagos(caminata.getId());

        assertNotNull(pagos);
        assertEquals(1, pagos.size());
    }

    /*
     * Prueba para obtener una instancia de Pago asociada a una instancia Caminata
     */
    @Test
    void testGetPago() throws EntityNotFoundException, IllegalOperationException
    {
        CaminataEntity caminata = listaCaminatas.get(0);
        PagoEntity pago = listaPagos.get(0);
        PagoEntity respuesta = caminataPagoService.getPago(caminata.getId(), pago.getId());

        assertEquals(pago.getId(), respuesta.getId());
        assertEquals(pago.getValorCaminata(), respuesta.getValorCaminata());
        assertEquals(pago.getValorTotal(), respuesta.getValorTotal());
        assertEquals(pago.getCaminante(), respuesta.getCaminante());
        assertEquals(pago.getCaminata(), respuesta.getCaminata());
        assertEquals(pago.getCcv(), respuesta.getCcv());
        assertEquals(pago.getCuotas(), respuesta.getCuotas());
        assertEquals(pago.getNumeroTarjeta(), respuesta.getNumeroTarjeta());
        assertEquals(pago.getFechaVencimiento(), respuesta.getFechaVencimiento());
        assertEquals(pago.getCuotas(), respuesta.getCuotas());
        assertEquals(pago.getPropietario(), respuesta.getPropietario());
    }

    /*
     * Prueba para obtener una instancia de Pago asociada a una Caminata
     */
    @Test
    void testGetPagoInvalidCaminata() throws EntityNotFoundException
    {
        assertThrows(EntityNotFoundException.class, ()->{
        
            PagoEntity pagoEntity = listaPagos.get(0);
            caminataPagoService.getPago(0L, pagoEntity.getId());
        });
    }

    /*
     * Prueba para obtener una instancia de Pago que no existe asociada a una caminata
     */
    @Test
    void testGetInvalidPagoCaminata()
    {
        assertThrows(EntityNotFoundException.class, () ->{
            CaminataEntity entity = listaCaminatas.get(0);
            caminataPagoService.getPago(entity.getId(), 0L);
        });
    }

    /*
     * Prueba para obtener una instancia de Pagos asociada a una instancia de Caminata que no le pertenece
     */
    @Test
    void getPagoNoAsociadoTest()
    {
        assertThrows(IllegalOperationException.class, () -> {
            CaminataEntity entity = listaCaminatas.get(0);
            PagoEntity pago = listaPagos.get(1);
            caminataPagoService.getPago(entity.getId(), pago.getId());
        });
    }

}


