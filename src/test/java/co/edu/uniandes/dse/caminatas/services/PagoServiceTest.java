package co.edu.uniandes.dse.caminatas.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(PagoService.class)
class PagoServiceTest 
{
    @Autowired
	private PagoService pagoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<PagoEntity> listaPagos = new ArrayList<>();
	private CaminanteEntity caminanteEntity;
    private EmpresaEntity empresaEntity;
    private CaminataEntity caminataEntity;

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
        entityManager.getEntityManager().createQuery("delete from EmpresaEntity");
        entityManager.getEntityManager().createQuery("delete from CaminataEntity");
	}

	private void insertData() 
    {
		
		caminanteEntity = factory.manufacturePojo(CaminanteEntity.class);
		entityManager.persist(caminanteEntity);

        empresaEntity = factory.manufacturePojo(EmpresaEntity.class);
		entityManager.persist(empresaEntity);

        for (int i = 0; i < 3; i++) 
		{
			PagoEntity pagoEntity = factory.manufacturePojo(PagoEntity.class);
			pagoEntity.setCaminante(caminanteEntity);
			entityManager.persist(pagoEntity);
			listaPagos.add(pagoEntity);
		}
        for (int x = 0; x < 3; x++) 
		{
			PagoEntity pagoEntity = factory.manufacturePojo(PagoEntity.class);
			pagoEntity.setEmpresa(empresaEntity);
			entityManager.persist(pagoEntity);
			listaPagos.add(pagoEntity);
		}
		caminataEntity = factory.manufacturePojo(CaminataEntity.class);
		entityManager.persist(caminataEntity);
		listaPagos.get(0).setCaminata(caminataEntity);
	}


	/**
	 * Prueba para crear un pago que tiene asociado un caminante
	 */
	@Test
	void testCrearPagoCaminante() throws EntityNotFoundException, IllegalOperationException 
	{
		PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
		nuevoPago.setCaminata(caminataEntity);
		nuevoPago.setCaminante(caminanteEntity);


		PagoEntity pagoCreado = pagoService.createPago(nuevoPago);
		assertNotNull(pagoCreado);

		System.out.println(pagoCreado);

		PagoEntity pagoGuardado = entityManager.find(PagoEntity.class, pagoCreado.getId());

		assertEquals(pagoCreado.getId(), pagoGuardado.getId());
		
	}

	/**
	 * Prueba para crear un pago que tiene asociado una empresa
	 */
	@Test
	void testCrearPagoEmpresa() throws EntityNotFoundException, IllegalOperationException 
	{
		PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
		nuevoPago.setCaminata(caminataEntity);
		nuevoPago.setEmpresa(empresaEntity);

		System.out.println(nuevoPago);

		PagoEntity pagoCreado = pagoService.createPago(nuevoPago);
		assertNotNull(pagoCreado);

		System.out.println(pagoCreado);

		PagoEntity pagoGuardado = entityManager.find(PagoEntity.class, pagoCreado.getId());

		assertEquals(pagoCreado.getId(), pagoGuardado.getId());
		
	}

	/**
	 * Prueba para crear un pago que no existe
	 */
	@Test
	void testCrearPagoNoExistente() throws EntityNotFoundException, IllegalOperationException 
	{
		assertThrows(EntityNotFoundException.class, () -> 
		{
			pagoService.createPago(null);
		});
	
	}

	/**
	 * Prueba para crear un pago que tiene un valor no valido
	 */
	@Test
	void testCrearPagoValorNoValido() throws EntityNotFoundException, IllegalOperationException 
	{
		assertThrows(IllegalOperationException.class, () -> 
		{
			PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
			nuevoPago.setValorCaminata(0);
			nuevoPago.setValorTotal(0);
			pagoService.createPago(nuevoPago);

		});
	}

	/**
	 * Prueba para crear un pago que tiene una tarjeta no valida
	 */
	@Test
	void testCrearPagoTarjetaNoValida() throws EntityNotFoundException, IllegalOperationException 
	{
		assertThrows(IllegalOperationException.class, () -> 
		{
			PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
			nuevoPago.setNumeroTarjeta("s");
			nuevoPago.setCcv("a");
			pagoService.createPago(nuevoPago);
		});
	}

	/**
	 * Prueba para crear un pago que no tiene asociado un usuario
	 */
	@Test
	void testCrearPagoSinUsuario() throws EntityNotFoundException, IllegalOperationException 
	{
		
		assertThrows(EntityNotFoundException.class, () -> 
		{
			PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
			nuevoPago.setCaminante(null);
			nuevoPago.setEmpresa(null);
			pagoService.createPago(nuevoPago);
		});
		
	}

	/**
	 * Prueba para crear un pago que tiene asociado a tipos dos usuarios a la vez
	 */
	@Test
	void testCrearPagoConDosUsuarios() throws EntityNotFoundException, IllegalOperationException 
	{
		
		assertThrows(IllegalOperationException.class, () -> 
		{
			PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
			nuevoPago.setCaminante(caminanteEntity);
			nuevoPago.setEmpresa(empresaEntity);
			pagoService.createPago(nuevoPago);
		});
		
	}

	/**
	 * Prueba para crear un pago que ya está registrado
	 */
	@Test
	void testCrearPagoYaExistente() throws EntityNotFoundException, IllegalOperationException 
	{
		
		assertThrows(IllegalOperationException.class, () -> 
		{
			PagoEntity nuevoPago = factory.manufacturePojo(PagoEntity.class);
			nuevoPago.setNumeroTransaccion(listaPagos.get(0).getNumeroTransaccion());
			pagoService.createPago(nuevoPago);
		});
		
	}

	/**
	 * Prueba para obtener todos los pagos
	 */
    @Test
	void testObtenerPagos()
    {
        List<PagoEntity> pagos = pagoService.getPagos();
		assertEquals(pagos.size(), listaPagos.size());
        for (PagoEntity pagoEntity : pagos)
		{
			boolean coincide = false; 
			for(PagoEntity pagoGuardado : listaPagos)
			{
				if(pagoEntity.getNumeroTransaccion().equals(pagoGuardado.getNumeroTransaccion()))
				{
					coincide = true;
				}
			}
			assertTrue(coincide);
		}
		
	}

	/**
	 * Prueba para consultar un pago
	 */
	@Test
	void testObtenerUnPago() throws EntityNotFoundException {
		PagoEntity pago = listaPagos.get(0);
		PagoEntity pagoEncontrado = pagoService.getPago(pago.getId());
		assertNotNull(pagoEncontrado);
		assertEquals(pago.getId(), pagoEncontrado.getId());
	}
	
	/**
	 * Prueba para consultar un pago que no existe
	 */
	@Test
	void testObtenerUnPagoInvalido() 
	{
		assertThrows(EntityNotFoundException.class,()->
		{
			pagoService.getPago(0L);
		});
	}

	/**
	 * Prueba para eliminar un pago
	 */
	@Test
	void testBorrarPago() throws EntityNotFoundException, IllegalOperationException 
	{
		PagoEntity pago = listaPagos.get(2);
		pagoService.deletePago(pago.getId());
		PagoEntity pagoEliminado = entityManager.find(PagoEntity.class, pago.getId());
		assertNull(pagoEliminado);
	}
	
	/**
	 * Prueba para eliminar un pago que no existe
	 */
	@Test
	void testBorrarPagoNoValido() {
		assertThrows(EntityNotFoundException.class, ()->
		{
			pagoService.deletePago(0L);
		});
	}

	/**
	 * Prueba para eliminar un pago ya realizado a una caminata
	 */
	@Test
	void testBorrarPagoConCaminata() {
		assertThrows(IllegalOperationException.class, () -> {
			PagoEntity pago = listaPagos.get(0);
			pagoService.deletePago(pago.getId());
		});
	}
}
