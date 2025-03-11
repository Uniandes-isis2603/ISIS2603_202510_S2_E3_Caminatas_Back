package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.SeguroDTO;
import co.edu.uniandes.dse.caminatas.dto.SeguroDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.SeguroService;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/seguros")
public class SeguroController {

    @Autowired
    private SeguroService seguroService;

    @Autowired
    private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos los seguros que existen en la aplicacion.
	 *
	 * @return JSONArray {@link SeguroDetailDTO} - Los seguros encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<SeguroDetailDTO> findAll(){
        List<SeguroEntity> seguros = seguroService.getSeguros();
        return modelMapper.map(seguros, new TypeToken<List<SeguroDetailDTO>>() {

        }.getType());
    }
    
    	/**
	 * Busca el seguro con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del premio que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link SeguroDetailDTO} - El premio buscado
	 * @throws WebApplicationException {@link WebApplicationExceptionMapper} - Error
	 *                                 de lógica que se genera cuando no se
	 *                                 encuentra el seguro.
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public SeguroDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		SeguroEntity seguroEntity = seguroService.getSeguro(id);
		return modelMapper.map(seguroEntity, SeguroDetailDTO.class);
	}
    
    	/**
	 * Crea un nuevo seguro con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param prize {@link SeguroDTO} - EL seguro que se desea guardar.
	 * @return JSON {@link SeguroDTO} - El seguro guardado con el atributo id
	 *         autogenerado.
             * @throws EntityNotFoundException 
         */
        @PostMapping
        @ResponseStatus(code = HttpStatus.CREATED)
        public SeguroDTO create(@RequestBody SeguroDTO seguroDTO) throws IllegalOperationException, EntityNotFoundException {
		SeguroEntity seguroEntity = seguroService.createSeguro(modelMapper.map(seguroDTO, SeguroEntity.class));
		return modelMapper.map(seguroEntity, SeguroDTO.class);
	}

    /**
     * Actualiza el seguro con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param id    Identificador del seguro que se desea actualizar. Este debe ser
     *              una cadena de dígitos.
     * @param seguro {@link SeguroDTO} El seguro que se desea guardar.
     * @return JSON {@link SeguroDetailDTO} - El seguro guardada.
          * @throws IllegalOperationException 
          */
         @PutMapping(value = "/{id}")
         @ResponseStatus(code = HttpStatus.OK)
         public SeguroDetailDTO update(@PathVariable Long id, @RequestBody SeguroDTO seguroDTO) throws EntityNotFoundException, IllegalOperationException {
        SeguroEntity seguroEntity = seguroService.updateSeguro(id, modelMapper.map(seguroDTO, SeguroEntity.class));
        return modelMapper.map(seguroEntity, SeguroDetailDTO.class);
    }

    /**
	 * Borra el seguro con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del seguro que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        seguroService.deleteSeguro(id);
    }
	

}
