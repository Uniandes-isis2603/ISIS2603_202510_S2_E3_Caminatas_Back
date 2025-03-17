package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
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
        void testCreateCompetencia() throws IllegalOperationException {
            CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
            caminataCompetencia.setDepartamento("Antioquia");

            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            caminataCompetencia.setFecha(calendario.getTime());

            LocalTime hora = LocalTime.now().plusHours(3);
            caminataCompetencia.setHora(hora);
            caminataCompetencia.setCondicionesParticipacion("Condiciones de participación");
            caminataCompetencia.setPremios("Premios");
            caminataCompetencia.setRequisitos("Requisitos");

            CaminataCompetenciaEntity result = caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
            assertEquals(caminataCompetencia.getCondicionesParticipacion(), entity.getCondicionesParticipacion());
            assertEquals(caminataCompetencia.getPremios(), entity.getPremios());
            assertEquals(caminataCompetencia.getRequisitos(), entity.getRequisitos());
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
                
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
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
                caminataCompetenciaService.CreateCompetencia(caminataCompetencia);
            });
        }

        @Test
        void testGetCaminatasCompetencia() 
        {
            List<CaminataCompetenciaEntity> caminatasCompetencia = caminataCompetenciaService.getCaminatasCompetencia();
            assertNotNull(caminatasCompetencia);
            assertFalse(caminatasCompetencia.isEmpty());
        }


        @Test
        void testGetCaminataCompetenciaById() throws EntityNotFoundException
        {
            CaminataCompetenciaEntity caminataCompetencia = caminatasCompetenciaList.get(0);
            CaminataCompetenciaEntity result = caminataCompetenciaService.getCaminataCompetencia(caminataCompetencia.getId());
            assertNotNull(result);
            assertEquals(caminataCompetencia.getId(), result.getId());
        }


        @Test
        void testGetCaminataCompetenciaByIdNotFound() 
        {
            assertThrows(EntityNotFoundException.class, () -> {
                caminataCompetenciaService.getCaminataCompetencia(999L);  // ID that does not exist
            });
        }


        @Test
        void testUpdateCaminataCompetencia() throws EntityNotFoundException, IllegalOperationException
        {
            CaminataCompetenciaEntity caminataCompetencia = caminatasCompetenciaList.get(0);
            caminataCompetencia.setTitulo("Nuevo Título");

            // Establecer la fecha y hora de la caminata
            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_YEAR, 1);  // Asegúrate de que la fecha sea al menos un día en el futuro
            caminataCompetencia.setFecha(calendario.getTime());

            LocalTime horaActual = LocalTime.now();
            LocalTime horaFutura = horaActual.plusMinutes(10);  // Asegura que la hora de la caminata sea después de la actual
            caminataCompetencia.setHora(horaFutura);

            // Asegúrate de que el departamento sea válido
            caminataCompetencia.setDepartamento("Antioquia"); // Asegúrate de que el departamento esté en la lista de departamentos permitidos

            CaminataCompetenciaEntity updatedCaminata = caminataCompetenciaService.updateCaminataCompetencia(caminataCompetencia.getId(), caminataCompetencia);

            assertNotNull(updatedCaminata);
            assertEquals("Nuevo Título", updatedCaminata.getTitulo());
        }


        @Test
        void testUpdateCaminataCompetenciaWithInvalidId() 
        {
            CaminataCompetenciaEntity caminataCompetencia = caminatasCompetenciaList.get(0);
            caminataCompetencia.setTitulo("Nuevo Título");

            assertThrows(EntityNotFoundException.class, () -> {
                caminataCompetenciaService.updateCaminataCompetencia(999L, caminataCompetencia); // ID that does not exist
            });
        }


        @Test
        void testDeleteCaminataCompetencia() throws EntityNotFoundException, IllegalOperationException
        {
            CaminataCompetenciaEntity caminataCompetencia = caminatasCompetenciaList.get(0);
            caminataCompetenciaService.deleteCaminataCompetencia(caminataCompetencia.getId());

            assertThrows(EntityNotFoundException.class, () -> {
                caminataCompetenciaService.getCaminataCompetencia(caminataCompetencia.getId()); // Should throw exception
            });
        }

        
        @Test
        void testDeleteCaminataCompetenciaWithPatrocinador() throws EntityNotFoundException
        {
            CaminataCompetenciaEntity caminataCompetencia = caminatasCompetenciaList.get(0);
            // Simulate associated patrocinador
            PatrocinadorEntity patrocinador = new PatrocinadorEntity();
            caminataCompetencia.setPatrocinador(patrocinador);
            entityManager.persist(patrocinador);

            assertThrows(IllegalOperationException.class, () -> {
                caminataCompetenciaService.deleteCaminataCompetencia(caminataCompetencia.getId());
            });
        }

    
}

