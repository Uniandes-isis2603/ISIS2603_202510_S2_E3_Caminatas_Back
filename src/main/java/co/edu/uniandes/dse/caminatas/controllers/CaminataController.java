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

import co.edu.uniandes.dse.caminatas.dto.CaminataDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminataDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminataService;

@RestController
@RequestMapping("/caminatas")
public class CaminataController 
{
    @Autowired
    private CaminataService caminataService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Busca y devuelve todas las caminatas que existen en la aplicacion.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminataDetailDTO> findAll()
    {
        List<CaminataEntity> caminatas = caminataService.getCaminatas();
        return modelMapper.map(caminatas, new TypeToken<List<CaminataDetailDTO>>(){}.getType());
    }

    /*
     * Busca la caminata con el id asociado recibido en la URL y lo devuelve.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException
    {
        CaminataEntity caminata = caminataService.getCaminata(id);
        return modelMapper.map(caminata, CaminataDetailDTO.class);
    }

    /*
     * Crea una nueva caminata con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CaminataDTO create(@RequestBody CaminataDTO caminataDTO) throws EntityNotFoundException, IllegalOperationException
    {
        CaminataEntity caminataEntity = caminataService.createCaminata(modelMapper.map(caminataDTO, CaminataEntity.class));
        return modelMapper.map(caminataEntity, CaminataDetailDTO.class);
    }

   /*
   * Actualiza la caminata con el id recibido en la URL con la información que se
    * recibe en el cuerpo de la petición. 
    */ 
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDTO update(@PathVariable Long id, @RequestBody CaminataDetailDTO caminata) throws EntityNotFoundException, IllegalOperationException
    {
        CaminataEntity caminataEntity = caminataService.updateCaminata(id, modelMapper.map(caminata, CaminataEntity.class));
        return modelMapper.map(caminataEntity, CaminataDetailDTO.class);
    }

    /*
     * Borra la caminata con el id asociado recibido en la URL.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException
    {
        caminataService.deleteCaminata(id);
    }
}
