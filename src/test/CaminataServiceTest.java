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

    }



}
