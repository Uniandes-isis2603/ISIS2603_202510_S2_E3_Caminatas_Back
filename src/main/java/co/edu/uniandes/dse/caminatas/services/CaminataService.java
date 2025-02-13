package co.edu.uniandes.dse.caminatas.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.CaminataRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminataService {

    @Autowired
    CaminataRepository caminataRepository;

    @Transactional
    public CaminataEntity createCaminata(CaminataEntity caminata) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia proceso de creación de la caminata");

        if(caminata.getTitulo() == null || caminata.getTitulo().isEmpty())
        {
            throw new IllegalOperationException("El título de la caminata no puede ser nulo o vacío.");
        }

        if(caminata.getId() == null || caminata.getId() == 0 || caminataRepository.existsById(caminata.getId()))
        {
            throw new IllegalOperationException("El id de la caminata no puede ser nulo o cero y debe ser único a cada caminata.");
        }

        if(caminata.getTipo() == "" || caminata.getTipo() == null)
        {
            throw new IllegalOperationException("El tipo de la caminata no puede ser nulo o vacío.");
        }

        Calendar calendario = Calendar.getInstance(); 

        if(caminata.getFecha() == null || caminata.getFecha().before(calendario.getTime()))
        {
            throw new IllegalOperationException("La fecha de la caminata no puede ser nula o anterior a la fecha actual.");
        }

        LocalDateTime horaActual = LocalDateTime.now();

        if(caminata.getHora() == null || caminata.getHora().isBefore(horaActual.toLocalTime()))
        {
            throw new IllegalOperationException("La hora de la caminata no puede ser nula o anterior a la actual.");
        }

        List<String> departamentos = new ArrayList<String> (Arrays.asList("Amazonas", "Antioquia", "Arauca", "Atlántico", "Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca", "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta", "Nariño", "Norte de Santander", "Putumayo", "Quindío", "Risaralda", "San Andrés y Providencia", "Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada"));

        if(caminata.getDepartamento() == null || caminata.getDepartamento().isEmpty() || !departamentos.contains(caminata.getDepartamento()))
        {
            throw new IllegalOperationException("El departamento de la caminata no puede ser nulo o vacío y debe pertenecer a Colombia.");
        }

        if(caminata.getCiudad() == null || caminata.getCiudad().isEmpty())
        {
            throw new IllegalOperationException("La ciudad de la caminata no puede ser nula o vacía.");
        }

        if(caminata.getDuracionEstimadaMinutos() <= 0)
        {
            throw new IllegalOperationException("La duración estimada de la caminata no puede ser menor o igual a cero.");
        }

        return caminataRepository.save(caminata);
    }

}
