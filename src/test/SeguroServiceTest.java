package co.edu.uniandes.dse.caminatas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;

@DataJpaTest
@Transactional
@Import(SeguroService.class)
class SeguroServiceTest {
    @Autowired
    private SeguroService seguroService;

    private PodamFactory factory = new PodamFactoryImpl();

    @Test
    void testCreateSeguro() {
        SeguroEntity seguro = factory.manufacturePojo(SeguroEntity.class);
        SeguroEntity result = seguroService.createSeguro(seguro);
        assertNotNull(result);

        SeguroEntity entity = entityManager.find(SeguroEntity.class, result.getId());
    }
}
