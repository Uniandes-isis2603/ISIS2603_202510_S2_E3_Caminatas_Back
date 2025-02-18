package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PatrocinadorService.class)
public class PatrocinadorServiceTest {

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private TestEntityManager entityManager;

    private List<PatrocinadorEntity> patrocinadoresList = new ArrayList<>();

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PatrocinadorEntity").executeUpdate();
    }

    private void insertData() {
        PatrocinadorEntity patrocinadorEntity = factory.manufacturePojo(PatrocinadorEntity.class);
        entityManager.persist(patrocinadorEntity);
        patrocinadoresList.add(patrocinadorEntity);
    }

@Test
void testCreatePatrocinador() throws IllegalOperationException {
    PatrocinadorEntity newEntity = factory.manufacturePojo(PatrocinadorEntity.class);
    newEntity.setNombre("Nombre Patrocinaor");
    newEntity.setDocumento("11223344");
    newEntity.setCorreo("patrocinador@example.com");
    newEntity.setTelefono("1234567890");
    
    PatrocinadorEntity result = patrocinadorService.createPatrocinador(newEntity);
    
    assertNotNull(result);
    PatrocinadorEntity entity = entityManager.find(PatrocinadorEntity.class, result.getId());
    assertEquals(newEntity.getId(), entity.getId());
    assertEquals(newEntity.getNombre(), entity.getNombre());
}

@Test
void testGetPatrocinadores() {
    List<PatrocinadorEntity> resultList = patrocinadorService.getPatrocinadores();
    
    assertEquals(patrocinadoresList.size(), resultList.size());
    for (PatrocinadorEntity entity : resultList) {
        assertTrue(patrocinadoresList.stream().anyMatch(p -> p.getId().equals(entity.getId())));
    }
}

@Test
void testGetPatrocinador() throws EntityNotFoundException {
    PatrocinadorEntity entity = patrocinadoresList.get(0);
    
    PatrocinadorEntity result = patrocinadorService.getPatrocinador(entity.getId());
    
    assertNotNull(result);
    assertEquals(entity.getId(), result.getId());
    assertEquals(entity.getNombre(), result.getNombre());
}

@Test
void testGetInvalidPatrocinador() {
    assertThrows(EntityNotFoundException.class, () -> {
        patrocinadorService.getPatrocinador(0L);
    });
}

@Test
void testUpdatePatrocinador() throws EntityNotFoundException, IllegalOperationException {
    PatrocinadorEntity entity = patrocinadoresList.get(0);
    PatrocinadorEntity updateEntity = factory.manufacturePojo(PatrocinadorEntity.class);
    
    updateEntity.setNombre("Patrocinador Actualizado");
    updateEntity.setDocumento("22334455");
    updateEntity.setCorreo("patrocinadorActualizado@example.com");
    updateEntity.setTelefono("0987654321");
    updateEntity.setId(entity.getId());
    
    
    PatrocinadorEntity result = patrocinadorService.updatePatrocinador(entity.getId(), updateEntity);
    
   
    assertNotNull(result);
    assertEquals(updateEntity.getNombre(), result.getNombre());
}

@Test
void testUpdateInvalidPatrocinador() {
    assertThrows(EntityNotFoundException.class, () -> {
        PatrocinadorEntity updateEntity = factory.manufacturePojo(PatrocinadorEntity.class);
        patrocinadorService.updatePatrocinador(0L, updateEntity);
    });
}

@Test
void testDeletePatrocinador() throws EntityNotFoundException, IllegalOperationException {
  
    PatrocinadorEntity entity = patrocinadoresList.get(0);
    
    patrocinadorService.deletePatrocinador(entity.getId());
    
    assertNull(entityManager.find(PatrocinadorEntity.class, entity.getId()));
}

@Test
void testDeleteInvalidPatrocinador() {
    assertThrows(EntityNotFoundException.class, () -> {
        patrocinadorService.deletePatrocinador(0L);
    });
}
}