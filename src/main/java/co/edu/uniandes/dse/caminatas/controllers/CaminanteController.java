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

import co.edu.uniandes.dse.caminatas.dto.CaminanteDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminanteDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminanteService;

@RestController
@RequestMapping("/caminantes")
public class CaminanteController {

    @Autowired
    private CaminanteService caminanteService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Busca y devuelve todos los caminantes que existen en la aplicación.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> findAll() {
        List<CaminanteEntity> caminantes = caminanteService.getCaminantes();
        return modelMapper.map(caminantes, new TypeToken<List<CaminanteDetailDTO>>() {}.getType());
    }

    /*
     * Busca el caminante con el id asociado recibido en la URL y lo devuelve.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminanteDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        CaminanteEntity caminante = caminanteService.getCaminante(id);
        return modelMapper.map(caminante, CaminanteDetailDTO.class);
    }

    /*
     * Crea un nuevo caminante con la información que se recibe en el cuerpo de la
     * petición y se regresa un objeto idéntico con un id auto-generado por la base
     * de datos.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CaminanteDetailDTO create(@RequestBody CaminanteDetailDTO caminante) throws EntityNotFoundException, IllegalOperationException {
        CaminanteEntity caminanteEntity = caminanteService.createCaminante(modelMapper.map(caminante, CaminanteEntity.class));
        return modelMapper.map(caminanteEntity, CaminanteDetailDTO.class);
    }

    /*
     * Actualiza el caminante con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminanteDTO update(@PathVariable Long id, @RequestBody CaminanteDetailDTO caminante) throws EntityNotFoundException, IllegalOperationException {
        CaminanteEntity caminanteEntity = caminanteService.updateCaminante(id, modelMapper.map(caminante, CaminanteEntity.class));
        return modelMapper.map(caminanteEntity, CaminanteDTO.class);
    }

    /*
     * Borra el caminante con el id asociado recibido en la URL.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        caminanteService.deleteCaminante(id);
    }
}
