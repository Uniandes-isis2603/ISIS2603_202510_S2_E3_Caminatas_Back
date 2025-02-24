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

import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CaminantePagoService.class})


public class CaminantePagoServiceTest 
{
    @Autowired
    private CaminantePagoService caminantePagoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CaminanteEntity> listaCaminantes = new ArrayList<>();
    private List<PagoEntity> listaPagos = new ArrayList<>();


    @BeforeEach
	void setUp()
    {
		clearData();
		insertData();
	}

	private void clearData() 
    {
		entityManager.getEntityManager().createQuery("delete from PagoEntity");
        entityManager.getEntityManager().createQuery("delete from CaminanteEntity");
	}

	private void insertData() 
    {
		
        for (int i = 0; i < 3; i++) 
		{
			CaminanteEntity caminanteEntity = factory.manufacturePojo(CaminanteEntity.class);
		    entityManager.persist(caminanteEntity);
            listaCaminantes.add(caminanteEntity);

            PagoEntity pagoEntity = factory.manufacturePojo(PagoEntity.class);
		    entityManager.persist(pagoEntity);
            listaPagos.add(pagoEntity);
            caminanteEntity.getPagos().add(pagoEntity);
		}

	}


    /*
     * Prueba para guardar un pago al caminante
     */
    @Test
    void testAñadirPagoCaminante() throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity caminante = listaCaminantes.get(0);
        PagoEntity pago = listaPagos.get(1);
        CaminanteEntity caminataConPago = caminantePagoService.addPago(caminante.getId(), pago.getId());

        assertNotNull(caminataConPago);
        assertTrue(caminataConPago.getPagos().contains(pago));

    }

    /*
     * Prueba para guardar un pago no valido al caminante
     */
    @Test
    void testAñadirPagoInvalido()
    {
        assertThrows(EntityNotFoundException.class, () ->{
            CaminanteEntity caminante = listaCaminantes.get(0);
            caminantePagoService.addPago(caminante.getId(), 0L);
        });
    }

    /*
     * Prueba para guardar un pago a un caminante no valido
     */
    @Test
    void testAñadirPagoACaminanteInvalido()
    {
        assertThrows(EntityNotFoundException.class, () ->{
            PagoEntity pago = listaPagos.get(0);
            caminantePagoService.addPago(0L,pago.getId());
        });
    }
    

    /*
     * Prueba para obtener todos los pagos del caminante
     */
    @Test
    void testObtenerPagos() throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity caminante = listaCaminantes.get(0);
        List<PagoEntity> pagos = caminantePagoService.listPagos(caminante.getId());

        assertNotNull(pagos);
        assertEquals(caminante.getPagos().size(), pagos.size());
    }

    /*
     * Prueba para obtener un pago especifico asociado al caminante
     */
    @Test
    void testObtenerPago() throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity caminante = listaCaminantes.get(0);
        PagoEntity pago = listaPagos.get(0);
        PagoEntity pagoObtenido = caminantePagoService.getPago(caminante.getId(), pago.getId());

        assertEquals(pago.getId(), pagoObtenido.getId());
        assertEquals(pago.getValorCaminata(), pagoObtenido.getValorCaminata());
        assertEquals(pago.getValorTotal(), pagoObtenido.getValorTotal());
        assertEquals(pago.getCaminante(), pagoObtenido.getCaminante());
        assertEquals(pago.getCaminata(), pagoObtenido.getCaminata());
        assertEquals(pago.getPropietario(), pagoObtenido.getPropietario());
    }

    /*
     * Prueba para obtener un pago no valido
     */
    @Test
    void testObtenerPagoDeCaminanteNoValido() throws EntityNotFoundException
    {
        assertThrows(EntityNotFoundException.class, ()->{
        
            PagoEntity pago = listaPagos.get(0);
            caminantePagoService.getPago(0L, pago.getId());
        });
    }

    /*
     * Prueba para obtener un pago no valido
     */
    @Test
    void testObtenerPagoNoValido() throws EntityNotFoundException
    {
        assertThrows(EntityNotFoundException.class, ()->{
        
            CaminanteEntity caminante = listaCaminantes.get(0);
            caminantePagoService.getPago(caminante.getId(), 0L);
        });
    }

    /*
     * Prueba para obtener un pago no asociado al caminante
     */
    @Test
    void testPagoNoAsociado()
    {
        assertThrows(IllegalOperationException.class, () -> {
            CaminanteEntity caminante = listaCaminantes.get(0);
            PagoEntity pago = listaPagos.get(1);
            caminantePagoService.getPago(caminante.getId(), pago.getId());
        });
    }

}