package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CaminataCompetenciaPatrocinadorService.class, CaminataCompetenciaService.class})
public class CaminataCompetenciaPatrocinadorServiceTest 
{
    @Autowired 
    private TestEntityManager entityManager;

    @Autowired
    private CaminataCompetenciaPatrocinadorService caminataCompetenciaPatrocinadorService;  

    @Autowired 
    private CaminataCompetenciaService caminataCompetenciaService;

    @Autowired 
    private PatrocinadorService patrocinadorService;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<CaminataCompetenciaEntity> caminatasCompetencias = new ArrayList<>();
    private List<PatrocinadorEntity> patrocinadores = new ArrayList<>();
    
    @BeforeEach
    void setUp()
    {
        clearData();
        insertData();
    }

    private void clearData()
    {
        entityManager.getEntityManager().createQuery("delete from CaminataCompetenciaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PatrocinadorEntity").executeUpdate();
    }

    /*
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas. 
     */
    private void insertData()
    {
        for(int i = 0; i < 3; i++)
        {
            CaminataCompetenciaEntity caminataCompetencia = factory.manufacturePojo(CaminataCompetenciaEntity.class);
            entityManager.persist(caminataCompetencia);
            caminatasCompetencias.add(caminataCompetencia);
        }
        for(int i = 0; i < 3; i++)
        {
            PatrocinadorEntity patrocinador = factory.manufacturePojo(PatrocinadorEntity.class);
            entityManager.persist(patrocinador);
            patrocinadores.add(patrocinador);
            if(i == 0)
            {
                caminatasCompetencias.get(i).setPatrocinador(patrocinador);
            }
        }
    }

    /*
     * Prueba para reemplazar el patrocinador de una caminata de competencia
     */
    @Test
    void testReplacePatrocinador() throws EntityNotFoundException {
        CaminataCompetenciaEntity caminata = caminatasCompetencias.get(0);
        PatrocinadorEntity nuevoPatrocinador = patrocinadores.get(1);
        
        caminataCompetenciaPatrocinadorService.replacePatrocinador(caminata.getId(), nuevoPatrocinador.getId());
        
        caminata = caminataCompetenciaService.getCaminataCompetencia(caminata.getId());
        assertEquals(caminata.getPatrocinador(), nuevoPatrocinador);
    }

}
