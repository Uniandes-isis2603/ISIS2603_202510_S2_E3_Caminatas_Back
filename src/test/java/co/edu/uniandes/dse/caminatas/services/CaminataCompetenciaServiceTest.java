package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(CaminataCompetenciaService.class)
public class CaminataCompetenciaServiceTest 
{

        @Autowired
        private CaminataCompetenciaService caminataCompetenciaService;

        @Autowired
        private TestEntityManager entityManager;

        private List<CaminataCompetenciaEntity> caminatasCompetenciaList = new ArrayList<>();

        private PodamFactory factory = new PodamFactoryImpl();


        @BeforeEach
        void setUp()
        {
            clearData();
            insertData();
        }

        private void clearData()
        {
            entityManager.getEntityManager().createQuery("delete from CaminataCompetenciaEntity").executeUpdate();
        }

        private void insertData()
        {
            CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
            entityManager.persist(caminataCompetencia);
            caminatasCompetenciaList.add(caminataCompetencia);
        }

        @Test
        void testCreateCaminataCompetencia() throws EntityNotFoundException, IllegalOperationException
        {
            CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
            CaminataCompetenciaEntity result = caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            assertNotNull(result);
            CaminataCompetenciaEntity entity = entityManager.find(CaminataCompetenciaEntity.class, result.getId());
            assertNotNull(entity);
            assertEquals(caminataCompetencia.getId(), entity.getId());
            assertEquals(caminataCompetencia.getTitulo(), entity.getTitulo());
            assertEquals(caminataCompetencia.getTipo(), entity.getTipo());
            assertEquals(caminataCompetencia.getFecha(), entity.getFecha());
            assertEquals(caminataCompetencia.getHora(), entity.getHora());
            assertEquals(caminataCompetencia.getDepartamento(), entity.getDepartamento());
            assertEquals(caminataCompetencia.getCiudad(), entity.getCiudad());
            assertEquals(caminataCompetencia.getDuracionEstimadaMinutos(), entity.getDuracionEstimadaMinutos());
        }

        /**
         * Prueba para crear una caminata con un título vacío.
         */
        @Test
        void testCreateCaminataTituloVacio()
        {
            assertThrows(IllegalOperationException.class, () -> 
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setTitulo("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setId(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setId((long) 0);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setId(caminatasCompetenciaList.get(0).getId());
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setTipo(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setTipo("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setFecha(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setFecha(new Date(0));
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setHora(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setHora(LocalTime.MIN);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setDepartamento(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setDepartamento("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setDepartamento("Mérida");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setCiudad(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setCiudad("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
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
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setDuracionEstimadaMinutos(0);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });

        }

        /**
         * Prueba para crear una caminata con un numero cero o negativo.
         */
        @Test
        void testCreateCaminataCompetenciaNumeroCero()
       {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setNumero(0);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
                
            });
        }

        /**
         * Prueba para crear una caminata con un número negativo.
         */
        @Test
        void testCreateCaminataCompetenciaNumeroNegativo()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setNumero(-1);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con condiciones de participación nulas.
         */
        @Test
        void testCreateCaminataCompetenciaCondicionesParticipacionNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setCondicionesParticipacion(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con condiciones de participación vacías.
         */
        @Test
        void testCreateCaminataCompetenciaCondicionesParticipacionVacia()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setCondicionesParticipacion("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con premios nulos.
         */
        @Test
        void testCreateCaminataCompetenciaPremiosNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setPremios(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con premios vacíos.
         */
        @Test
        void testCreateCaminataCompetenciaPremiosVacios()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setPremios("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con requisitos nulos.
         */
        @Test
        void testCreateCaminataCompetenciaRequisitosNull()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setRequisitos(null);
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }

        /**
         * Prueba para crear una caminata con requisitos vacíos.
         */
        @Test
        void testCreateCaminataCompetenciaRequisitosVacios()
        {
            assertThrows(IllegalOperationException.class, () ->
            {
                CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
                caminataCompetencia.setRequisitos("");
                caminataCompetenciaService.createCaminataCompetencia(caminataCompetencia);
            });
        }
    
}
