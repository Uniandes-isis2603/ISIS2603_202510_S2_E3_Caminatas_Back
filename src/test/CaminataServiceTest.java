package co.edu.uniandes.dse.caminatas.services;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.transaction.Transactional;

public class CaminataServiceTest 
{
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
            entityManager.persist(camiantaEntity);
            caminatasList.add(camiantaEntity);
        }

        @Test
        void testCreateCaminata() throws EntityNotFoundException, IllegalOperationException
        {
            CaminataEntity caminata = factory.manufacturePojo(CaminataEntity.class);
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
                caminata.setId(0);
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

    }

}
