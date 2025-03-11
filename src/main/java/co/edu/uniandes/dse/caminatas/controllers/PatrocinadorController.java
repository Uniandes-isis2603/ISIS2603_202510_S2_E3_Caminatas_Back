package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.PatrocinadorDTO;
import co.edu.uniandes.dse.caminatas.dto.PatrocinadorDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.PatrocinadorEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.PatrocinadorService;

/**
 * Clase que implementa el recurso "patrocinadores".
 * 
 * @author Universidad de los Andes
 */
@RestController
@RequestMapping("/patrocinadores")
public class PatrocinadorController {

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todos los patrocinadores que existen en la aplicación.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PatrocinadorDetailDTO> findAll() {
        List<PatrocinadorEntity> patrocinadores = patrocinadorService.getPatrocinadores();
        return modelMapper.map(patrocinadores, new TypeToken<List<PatrocinadorDetailDTO>>() {}.getType());
    }

    /**
     * Busca el patrocinador con el id asociado recibido en la URL y lo devuelve.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PatrocinadorDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PatrocinadorEntity patrocinador = patrocinadorService.getPatrocinador(id);
        return modelMapper.map(patrocinador, PatrocinadorDetailDTO.class);
    }

    /**
     * Crea un nuevo patrocinador con la información que se recibe en el cuerpo de la petición.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PatrocinadorDTO create(@RequestBody PatrocinadorDTO patrocinador) throws IllegalOperationException {
        PatrocinadorEntity patrocinadorEntity = patrocinadorService.createPatrocinador(modelMapper.map(patrocinador, PatrocinadorEntity.class));
        return modelMapper.map(patrocinadorEntity, PatrocinadorDTO.class);
    }

    /**
     * Actualiza el patrocinador con el id recibido en la URL con la información que se recibe en la petición.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PatrocinadorDTO update(@PathVariable Long id, @RequestBody PatrocinadorDetailDTO patrocinador) throws EntityNotFoundException, IllegalOperationException {
        PatrocinadorEntity patrocinadorEntity = patrocinadorService.updatePatrocinador(id, modelMapper.map(patrocinador, PatrocinadorEntity.class));
        return modelMapper.map(patrocinadorEntity, PatrocinadorDetailDTO.class);
    }

    /**
     * Borra el patrocinador con el id asociado recibido en la URL.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        patrocinadorService.deletePatrocinador(id);
    }
}