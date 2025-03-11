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

import co.edu.uniandes.dse.caminatas.dto.CaminataCompetenciaDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminataCompetenciaDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminataCompetenciaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminataCompetenciaService;

/**
 * Clase que implementa el recurso "caminataCompetencias".
 */
@RestController
@RequestMapping("/caminataCompetencias")
public class CaminataCompetenciaController {

    @Autowired
    private CaminataCompetenciaService caminataCompetenciaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todas las caminatas en competencia.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminataCompetenciaDetailDTO> findAll() {
        List<CaminataCompetenciaEntity> caminatas = caminataCompetenciaService.getCaminatasCompetencia();
        return modelMapper.map(caminatas, new TypeToken<List<CaminataCompetenciaDetailDTO>>() {}.getType());
    }

    /**
     * Busca la caminata en competencia con el id recibido en la URL y la devuelve.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataCompetenciaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        CaminataCompetenciaEntity caminata = caminataCompetenciaService.getCaminataCompetencia(id);
        return modelMapper.map(caminata, CaminataCompetenciaDetailDTO.class);
    }

    /**
     * Crea una nueva caminata en competencia con la información recibida en el cuerpo de la petición.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CaminataCompetenciaDTO create(@RequestBody CaminataCompetenciaDTO caminata) throws IllegalOperationException {
        CaminataCompetenciaEntity caminataEntity = caminataCompetenciaService.CreateCompetencia(modelMapper.map(caminata, CaminataCompetenciaEntity.class));
        return modelMapper.map(caminataEntity, CaminataCompetenciaDTO.class);
    }

    /**
     * Actualiza la caminata en competencia con el id recibido en la URL con la información de la petición.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataCompetenciaDTO update(@PathVariable Long id, @RequestBody CaminataCompetenciaDetailDTO caminata) throws EntityNotFoundException, IllegalOperationException {
        CaminataCompetenciaEntity caminataEntity = caminataCompetenciaService.updateCaminataCompetencia(id, modelMapper.map(caminata, CaminataCompetenciaEntity.class));
        return modelMapper.map(caminataEntity, CaminataCompetenciaDetailDTO.class);
    }

    /**
     * Borra la caminata en competencia con el id asociado recibido en la URL.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        caminataCompetenciaService.deleteCaminataCompetencia(id);
    }
}
