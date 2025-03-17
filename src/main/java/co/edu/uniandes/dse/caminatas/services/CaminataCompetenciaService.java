package co.edu.uniandes.dse.caminatas.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataCompetenciaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataCompetenciaService 
{
    @Autowired
    CaminataCompetenciaRepository caminataCompetenciaRepository;

    @Transactional
    public CaminataCompetenciaEntity CreateCompetencia(CaminataCompetenciaEntity caminataCompetencia) throws IllegalOperationException
    {
        log.info("Inicia proceso de creación de una caminata de competencia");

        if(caminataCompetencia.getTitulo() == null || caminataCompetencia.getTitulo().isEmpty())
        {
            throw new IllegalOperationException("El título de la caminata no puede ser nulo o vacío.");
        }

        if(caminataCompetencia.getTipo() == "" || caminataCompetencia.getTipo() == null)
        {
            throw new IllegalOperationException("El tipo de la caminata no puede ser nulo o vacío.");
        }

        Calendar calendario = Calendar.getInstance(); 

        if(caminataCompetencia.getFecha() == null || caminataCompetencia.getFecha().before(calendario.getTime()))
        {
            throw new IllegalOperationException("La fecha de la caminata no puede ser nula o anterior a la fecha actual.");
        }

        if(caminataCompetencia.getHora() == null)
        {
            throw new IllegalOperationException("La hora de la caminata no puede ser nula.");
        }

        LocalDateTime fechaHoraEvento = LocalDateTime.of(
        caminataCompetencia.getFecha().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        caminataCompetencia.getHora()
        );
        LocalDateTime ahora = LocalDateTime.now();
        if (fechaHoraEvento.isBefore(ahora)) {
            throw new IllegalOperationException("La fecha y hora de la caminata debe ser posterior a la actual.");
        }

        List<String> departamentos = new ArrayList<String> (Arrays.asList("Amazonas", "Antioquia", "Arauca", "Atlántico", "Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca", "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindío", "Risaralda", "San Andrés y Providencia", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada"));

        if(caminataCompetencia.getDepartamento() == null || caminataCompetencia.getDepartamento().isEmpty() || !departamentos.contains(caminataCompetencia.getDepartamento()))
        {
            throw new IllegalOperationException("El departamento de la caminata no puede ser nulo o vacío y debe pertenecer a Colombia.");
        }

        if(caminataCompetencia.getCiudad() == null || caminataCompetencia.getCiudad().isEmpty())
        {
            throw new IllegalOperationException("La ciudad de la caminata no puede ser nula o vacía.");
        }

        if(caminataCompetencia.getDuracionEstimadaMinutos() <= 0)
        {
            throw new IllegalOperationException("La duración estimada de la caminata no puede ser menor o igual a cero.");
        }

        if(caminataCompetencia.getNumero() <= 0)
        {
            throw new IllegalOperationException("El número de la caminata no puede ser menor o igual a cero.");
        }

        if(caminataCompetencia.getCondicionesParticipacion() == null || caminataCompetencia.getCondicionesParticipacion().isEmpty())
        {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden ser nulas o vacías.");
        }

        if(caminataCompetencia.getCondicionesParticipacion().length() > 1000) 
        {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden tener más de 1000 caracteres.");
        }

        if(caminataCompetencia.getPremios() == null || caminataCompetencia.getPremios().isEmpty())
        {
            throw new IllegalOperationException("Los premios de la caminata no pueden ser nulos o vacíos.");
        }

        if(caminataCompetencia.getRequisitos() == null || caminataCompetencia.getRequisitos().isEmpty())
        {
            throw new IllegalOperationException("Los requisitos de la caminata no pueden ser nulos o vacíos.");
        }
        
        return caminataCompetenciaRepository.save(caminataCompetencia);   
    }
    
    /*
     * Devuelve todas las caminatas de competencia en la base de datos
     */
    @Transactional 
    public List<CaminataCompetenciaEntity> getCaminatasCompetencia()
    {
        log.info("Inicia proceso de consultar todas las caminatas de competencia");
        return caminataCompetenciaRepository.findAll();
    }

    /*
     * Busca una caminata de competencia por ID
     */
    @Transactional
    public CaminataCompetenciaEntity getCaminataCompetencia(Long caminataCompetenciaId) throws EntityNotFoundException
    {
        log.info("Inicia proceso de consultar la caminata de competencia con id = {0}", caminataCompetenciaId);
        Optional<CaminataCompetenciaEntity> caminataCompetencia = caminataCompetenciaRepository.findById(caminataCompetenciaId);
        if (caminataCompetencia.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la caminata de competencia con el id = " + caminataCompetenciaId);
        }
        log.info("Termina proceso de consultar la caminata de competencia con id = {0}", caminataCompetenciaId);
        return caminataCompetencia.get();
    }

    /*
     * Actualiza la información de una caminata de competencia
     */
    @Transactional
    public CaminataCompetenciaEntity updateCaminataCompetencia(Long caminataCompetenciaId, CaminataCompetenciaEntity caminataCompetencia) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de actualizar la caminata de competencia con id = {0}", caminataCompetenciaId);
        Optional<CaminataCompetenciaEntity> caminataCompetenciaEntity = caminataCompetenciaRepository.findById(caminataCompetenciaId);
        if (caminataCompetenciaEntity.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la caminata de competencia con el id = " + caminataCompetenciaId);
        }

        if(caminataCompetencia.getTitulo() == null || caminataCompetencia.getTitulo().isEmpty())
        {
            throw new IllegalOperationException("El título de la caminata no puede ser nulo o vacío.");
        }

        if(caminataCompetencia.getTipo() == "" || caminataCompetencia.getTipo() == null)
        {
            throw new IllegalOperationException("El tipo de la caminata no puede ser nulo o vacío.");
        }

        Calendar calendario = Calendar.getInstance(); 

        if(caminataCompetencia.getFecha() == null || caminataCompetencia.getFecha().before(calendario.getTime()))
        {
            throw new IllegalOperationException("La fecha de la caminata no puede ser nula o anterior a la fecha actual.");
        }

        LocalDateTime horaActual = LocalDateTime.now();

        if(caminataCompetencia.getHora() == null || caminataCompetencia.getHora().isBefore(horaActual.toLocalTime()))
        {
            throw new IllegalOperationException("La hora de la caminata no puede ser nula o anterior a la actual.");
        }

        List<String> departamentos = new ArrayList<String> (Arrays.asList("Amazonas", "Antioquia", "Arauca", "Atlántico", "Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca", "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindío", "Risaralda", "San Andrés y Providencia", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada"));

        if(caminataCompetencia.getDepartamento() == null || caminataCompetencia.getDepartamento().isEmpty() || !departamentos.contains(caminataCompetencia.getDepartamento()))
        {
            throw new IllegalOperationException("El departamento de la caminata no puede ser nulo o vacío y debe pertenecer a Colombia.");
        }

        if(caminataCompetencia.getCiudad() == null || caminataCompetencia.getCiudad().isEmpty())
        {
            throw new IllegalOperationException("La ciudad de la caminata no puede ser nula o vacía.");
        }

        if(caminataCompetencia.getDuracionEstimadaMinutos() <= 0)
        {
            throw new IllegalOperationException("La duración estimada de la caminata no puede ser menor o igual a cero.");
        }

        if(caminataCompetencia.getNumero() <= 0)
        {
            throw new IllegalOperationException("El número de la caminata no puede ser menor o igual a cero.");
        }

        if(caminataCompetencia.getCondicionesParticipacion() == null || caminataCompetencia.getCondicionesParticipacion().isEmpty())
        {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden ser nulas o vacías.");
        }

        if(caminataCompetencia.getCondicionesParticipacion().length() > 1000) 
        {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden tener más de 1000 caracteres.");
        }

        if(caminataCompetencia.getPremios() == null || caminataCompetencia.getPremios().isEmpty())
        {
            throw new IllegalOperationException("Los premios de la caminata no pueden ser nulos o vacíos.");
        }

        if(caminataCompetencia.getRequisitos() == null || caminataCompetencia.getRequisitos().isEmpty())
        {
            throw new IllegalOperationException("Los requisitos de la caminata no pueden ser nulos o vacíos.");
        }
       
        caminataCompetencia.setId(caminataCompetenciaId);
        log.info("Termina proceso de actualizar la caminata de competencia con id = {0}", caminataCompetenciaId);
        return caminataCompetenciaRepository.save(caminataCompetencia);   
    }

    /*
     * Elimina una caminata de competencia por ID
     */
    @Transactional
    public void deleteCaminataCompetencia(Long caminataCompetenciaId) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de eliminación de la caminata de competencia con id = {0}", caminataCompetenciaId);
        Optional<CaminataCompetenciaEntity> caminataCompetencia = caminataCompetenciaRepository.findById(caminataCompetenciaId);
        if (caminataCompetencia.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la caminata de competencia con el id = " + caminataCompetenciaId);
        }

        PatrocinadorEntity patrocinador = caminataCompetencia.get().getPatrocinador();
        if(patrocinador != null)
        {
            throw new IllegalOperationException("No se puede eliminar esta caminata ya que se encuentra asociada a un patrocinador.");
        }
        caminataCompetenciaRepository.deleteById(caminataCompetenciaId);
        log.info("Termina proceso de eliminación de la caminata de competencia con id = {0}", caminataCompetenciaId);
    }
}
