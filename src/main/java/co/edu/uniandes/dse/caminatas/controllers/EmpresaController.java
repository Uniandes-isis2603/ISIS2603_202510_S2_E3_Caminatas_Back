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

import co.edu.uniandes.dse.caminatas.dto.EmpresaDTO;
import co.edu.uniandes.dse.caminatas.dto.EmpresaDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.EmpresaService;

/**
 * Clase que implementa el recurso "empresas".
 */
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todas las empresas que existen en la aplicación.
     *
     * @return JSONArray {@link EmpresaDetailDTO} - Las empresas encontradas en la
     *         aplicación. Si no hay ninguna retorna una lista vacía.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EmpresaDetailDTO> findAll() {
        List<EmpresaEntity> empresas = empresaService.getEmpresas();
        return modelMapper.map(empresas, new TypeToken<List<EmpresaDetailDTO>>() {
        }.getType());
    }

    /**
     * Busca la empresa con el id asociado recibido en la URL y la devuelve.
     *
     * @param id Identificador de la empresa que se está buscando. Este debe ser una
     *           cadena de dígitos.
     * @return JSON {@link EmpresaDetailDTO} - La empresa buscada
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmpresaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EmpresaEntity empresaEntity = empresaService.getEmpresa(id);
        return modelMapper.map(empresaEntity, EmpresaDetailDTO.class);
    }

    /**
     * Crea una nueva empresa con la información que se recibe en el cuerpo de la
     * petición y se regresa un objeto idéntico con un id auto-generado por la base
     * de datos.
     *
     * @param empresaDTO {@link EmpresaDTO} - La empresa que se desea guardar.
     * @return JSON {@link EmpresaDTO} - La empresa guardada con el atributo id
     *         autogenerado.
     * @throws IllegalOperationException 
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EmpresaDTO create(@RequestBody EmpresaDTO empresaDTO) throws IllegalOperationException {
        EmpresaEntity empresaEntity = empresaService.createEmpresa(modelMapper.map(empresaDTO, EmpresaEntity.class));
        return modelMapper.map(empresaEntity, EmpresaDTO.class);
    }

    /**
     * Actualiza la empresa con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param id     Identificador de la empresa que se desea actualizar. Este debe ser
     *               una cadena de dígitos.
     * @param empresaDTO {@link EmpresaDTO} La empresa que se desea guardar.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmpresaDTO update(@PathVariable Long id, @RequestBody EmpresaDTO empresaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        EmpresaEntity empresaEntity = empresaService.updateEmpresa(id, modelMapper.map(empresaDTO, EmpresaEntity.class));
        return modelMapper.map(empresaEntity, EmpresaDTO.class);
    }

    /**
     * Borra la empresa con el id asociado recibido en la URL.
     *
     * @param id Identificador de la empresa que se desea borrar. Este debe ser una
     *           cadena de dígitos.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        empresaService.deleteEmpresa(id);
    }
}