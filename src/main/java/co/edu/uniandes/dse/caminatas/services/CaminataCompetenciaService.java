package co.edu.uniandes.dse.caminatas.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private static final String MENSAJE_1 = "No se encontró la caminata de competencia con el id = ";

    @Autowired
    CaminataCompetenciaRepository caminataCompetenciaRepository;

    @Transactional
    public CaminataCompetenciaEntity CreateCompetencia(CaminataCompetenciaEntity caminataCompetencia) throws IllegalOperationException
    {
        log.info("Inicia proceso de creación de una caminata de competencia");
        validarTitulo(caminataCompetencia.getTitulo());
        validarTipo(caminataCompetencia.getTipo());
        validarFechaYHora(caminataCompetencia.getFecha(), caminataCompetencia.getHora());
        validarDepartamento(caminataCompetencia.getDepartamento());
        validarCiudad(caminataCompetencia.getCiudad());
        validarDuracion(caminataCompetencia.getDuracionEstimadaMinutos());
        validarNumero(caminataCompetencia.getNumero());
        validarCondiciones(caminataCompetencia.getCondicionesParticipacion());
        validarPremios(caminataCompetencia.getPremios());
        validarRequisitos(caminataCompetencia.getRequisitos());

        return caminataCompetenciaRepository.save(caminataCompetencia);   
    }

    private void validarTitulo(String titulo) throws IllegalOperationException {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalOperationException("El título de la caminata no puede ser nulo o vacío.");
        }
    }

    private void validarTipo(String tipo) throws IllegalOperationException {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalOperationException("El tipo de la caminata no puede ser nulo o vacío.");
        }
    }

    private void validarFechaYHora(java.util.Date fecha, java.time.LocalTime hora) throws IllegalOperationException {
        Calendar calendario = Calendar.getInstance(); 
        if (fecha == null || fecha.before(calendario.getTime())) {
            throw new IllegalOperationException("La fecha de la caminata no puede ser nula o anterior a la fecha actual.");
        }
        if (hora == null) {
            throw new IllegalOperationException("La hora de la caminata no puede ser nula.");
        }
        LocalDateTime fechaHoraEvento = LocalDateTime.of(
            fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            hora
        );
        LocalDateTime ahora = LocalDateTime.now();
        if (fechaHoraEvento.isBefore(ahora)) {
            throw new IllegalOperationException("La fecha y hora de la caminata debe ser posterior a la actual.");
        }
    }

    private void validarDepartamento(String departamento) throws IllegalOperationException {
        List<String> departamentos = Arrays.asList(
            "Amazonas", "Antioquia", "Arauca", "Atlántico", "Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca", "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindío", "Risaralda", "San Andrés y Providencia", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada"
        );
        if (departamento == null || departamento.isEmpty() || !departamentos.contains(departamento)) {
            throw new IllegalOperationException("El departamento de la caminata no puede ser nulo o vacío y debe pertenecer a Colombia.");
        }
    }

    private void validarCiudad(String ciudad) throws IllegalOperationException {
        if (ciudad == null || ciudad.isEmpty()) {
            throw new IllegalOperationException("La ciudad de la caminata no puede ser nula o vacía.");
        }
    }

    private void validarDuracion(float duracion) throws IllegalOperationException {
        if (duracion <= 0) {
            throw new IllegalOperationException("La duración estimada de la caminata no puede ser menor o igual a cero.");
        }
    }

    private void validarNumero(int numero) throws IllegalOperationException {
        if (numero <= 0) {
            throw new IllegalOperationException("El número de la caminata no puede ser menor o igual a cero.");
        }
    }

    private void validarCondiciones(String condiciones) throws IllegalOperationException {
        if (condiciones == null || condiciones.isEmpty()) {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden ser nulas o vacías.");
        }
        if (condiciones.length() > 1000) {
            throw new IllegalOperationException("Las condiciones de participación de la caminata no pueden tener más de 1000 caracteres.");
        }
    }

    private void validarPremios(String premios) throws IllegalOperationException {
        if (premios == null || premios.isEmpty()) {
            throw new IllegalOperationException("Los premios de la caminata no pueden ser nulos o vacíos.");
        }
    }

    private void validarRequisitos(String requisitos) throws IllegalOperationException {
        if (requisitos == null || requisitos.isEmpty()) {
            throw new IllegalOperationException("Los requisitos de la caminata no pueden ser nulos o vacíos.");
        }
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
            throw new EntityNotFoundException(MENSAJE_1 + caminataCompetenciaId);
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
            throw new EntityNotFoundException(MENSAJE_1 + caminataCompetenciaId);
        }

        validarCaminataCompetencia(caminataCompetencia);

        caminataCompetencia.setId(caminataCompetenciaId);
        log.info("Termina proceso de actualizar la caminata de competencia con id = {0}", caminataCompetenciaId);
        return caminataCompetenciaRepository.save(caminataCompetencia);   
    }

    private void validarCaminataCompetencia(CaminataCompetenciaEntity caminataCompetencia) throws IllegalOperationException {
        validarTitulo(caminataCompetencia.getTitulo());
        validarTipo(caminataCompetencia.getTipo());
        validarFechaYHora(caminataCompetencia.getFecha(), caminataCompetencia.getHora());
        validarDepartamento(caminataCompetencia.getDepartamento());
        validarCiudad(caminataCompetencia.getCiudad());
        validarDuracion(caminataCompetencia.getDuracionEstimadaMinutos());
        validarNumero(caminataCompetencia.getNumero());
        validarCondiciones(caminataCompetencia.getCondicionesParticipacion());
        validarPremios(caminataCompetencia.getPremios());
        validarRequisitos(caminataCompetencia.getRequisitos());
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
            throw new EntityNotFoundException(MENSAJE_1 + caminataCompetenciaId);
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
