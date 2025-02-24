package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CaminataService.class)
public class CaminataServiceTest 
{

        @Autowired
        private CaminataService caminataService;

        @Autowired
        private TestEntityManager entityManager;

        private List<CaminataEntity> caminatasList = new ArrayList<>();

         private PodamFactory factory = new PodamFactoryImpl();

        @BeforeEach
        void setUp()
        {
            clearData();
            insertData();
        }

        private void clearData()
        {
            entityManager.getEntityManager().createQuery("delete from CaminataEntity").executeUpdate();
        }

        private void insertData()
        {
            CaminataEntity caminataEntity = factory.manufacturePojo(CaminataEntity.class);
            entityManager.persist(caminataEntity);
            caminatasList.add(caminataEntity);
        }

        @Test
        void testCreateCaminata() throws EntityNotFoundException, IllegalOperationException
        {
            
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
            caminata.setDepartamento("Antioquia");

            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            caminata.setFecha(calendario.getTime());

            LocalTime hora = LocalTime.now().plusHours(1); 
            caminata.setHora(hora);
            CaminataEntity result = caminataService.createCaminata(caminata);
            assertNotNull(result);
        

            CaminataEntity entity = entityManager.find(CaminataEntity.class, result.getId());
            assertNotNull(entity);
            assertEquals(caminata.getId(), entity.getId());
            assertEquals(caminata.getTitulo(), entity.getTitulo());
            assertEquals(caminata.getTipo(), entity.getTipo());
            assertEquals(caminata.getFecha(), entity.getFecha());
            assertEquals(caminata.getHora(), entity.getHora());
            assertEquals(caminata.getDepartamento(), entity.getDepartamento());
            assertEquals(caminata.getCiudad(), entity.getCiudad());
            assertEquals(caminata.getDuracionEstimadaMinutos(), entity.getDuracionEstimadaMinutos());
        }

        /**
         * Prueba para crear una caminata con un título nulo.
         */

        @Test
        void testCreateCaminataTituloNull() throws EntityNotFoundException, IllegalOperationException
        {
            assertThrows(IllegalOperationException.class, () -> 
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setTitulo(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un título vacío.
         */
        @Test
        void testCreateCaminataTituloVacio()
        {
            assertThrows(IllegalOperationException.class, () -> 
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setTitulo("");
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un id nulo.
         */
        @Test
        void testCreateCaminataIdNull() 
        {
            assertThrows(IllegalOperationException.class, () -> 
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setId(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un id cero.
         */
        @Test
        void testeCreateCaminataIdCero()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setId((long) 0);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un id ya existente.
         */
        @Test
        void testCreateCaminataIdExistente()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setId(caminatasList.get(0).getId());
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un tipo nulo.
         */
        @Test   
        void testCreateCaminataTipoNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setTipo(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un tipo vacío.
         */
        @Test
        void testCreateCaminataTipoVacio()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setTipo("");
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una fecha nula.
         */
        @Test
        void testCreateCaminataFechaNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setFecha(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una fecha anterior a la actual.
         */
        @Test
        void testCreateCaminataFechaAnterior()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setFecha(new Date(0));
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una hora nula.
         */
        @Test
        void testCreateCaminataHoraNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setHora(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una hora anterior a la actual.
         */
        @Test
        void testCreateCaminataHoraAnterior()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setHora(LocalTime.MIN);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un departamento nulo.
         */
        @Test
        void testCreateCaminataDepartamentoNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setDepartamento(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un departamento vacío.
         */
        @Test
        void testCreateCaminataDepartamentoVacio()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setDepartamento("");
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con un departamento que no pertenece a Colombia.
         */
        @Test
        void testCreateCaminataDepartamentoNoColombia()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setDepartamento("Mérida");
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una ciudad nula.
         */
        @Test
        void testCreateCaminataCiudadNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setCiudad(null);
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una ciudad vacía.
         */
        @Test
        void testCreateCaminataCiudadVacia()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setCiudad("");
                caminataService.createCaminata(caminata);
            });
        }

        /**
         * Prueba para crear una caminata con una duración estimada menor o igual a cero.
         */
        @Test
        void testCreateCaminataDuracionEstimadaCero()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
                caminata.setDuracionEstimadaMinutos(0);
                caminataService.createCaminata(caminata);
            });

        }

        /*
         * Prueba para actualizar una caminata
         */
        @Test
        void testUpdateCaminata() throws EntityNotFoundException, IllegalOperationException {
            CaminataEntity caminata = caminatasList.get(0);
            CaminataEntity entity = factory.manufacturePojo(CaminataEntity.class);
            
            entity.setId(caminata.getId());

            // 🔹 Asegurar que la fecha sea válida (posterior a la actual)
            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            entity.setFecha(calendario.getTime());

            // 🔹 Asegurar que la hora también sea válida
            entity.setHora(LocalTime.now().plusHours(1));

            // 🔹 Asignar un departamento válido (ejemplo: "Cundinamarca")
            entity.setDepartamento("Cundinamarca");

            caminataService.updateCaminata(caminata.getId(), entity);
            CaminataEntity updated = entityManager.find(CaminataEntity.class, caminata.getId());

            assertNotNull(updated);
            assertEquals(entity.getId(), updated.getId());
            assertEquals(entity.getTitulo(), updated.getTitulo());
            assertEquals(entity.getTipo(), updated.getTipo());
            assertEquals(entity.getFecha(), updated.getFecha());
            assertEquals(entity.getHora(), updated.getHora());
            assertEquals(entity.getDepartamento(), updated.getDepartamento());
            assertEquals(entity.getCiudad(), updated.getCiudad());
            assertEquals(entity.getDuracionEstimadaMinutos(), updated.getDuracionEstimadaMinutos());
        }

        /**
         * Prueba para actualizar una caminata no existente
         */
        @Test
        void testUpdateCaminataInvalida() throws EntityNotFoundException {
            CaminataEntity caminata = caminatasList.get(0); 
            assertThrows(IllegalOperationException.class, () -> {
                CaminataEntity pojoEntity = factory.manufacturePojo(CaminataEntity.class);
                pojoEntity.setId(caminata.getId()); 
                pojoEntity.setDepartamento(""); 
                caminataService.updateCaminata(caminata.getId(), pojoEntity);
            });
        }

        /*
         * Prueba para eliminar una caminata
         */
        @Test
        void testDeleteCaminata() throws EntityNotFoundException, IllegalOperationException {
            CaminataEntity caminata = caminatasList.get(0);
            caminataService.deleteCaminata(caminata.getId());
            CaminataEntity deleted = entityManager.find(CaminataEntity.class, caminata.getId());
            assertNull(deleted);
        }

        /*
         * Prueba para eliminar una caminata no existente
         */
        @Test
        void testDeleteCaminataInvalida() throws EntityNotFoundException {
            assertThrows(EntityNotFoundException.class, () -> {
                caminataService.deleteCaminata(0L); 
            });
        }

    }


