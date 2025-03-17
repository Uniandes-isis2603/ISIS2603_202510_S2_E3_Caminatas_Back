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
import co.edu.uniandes.dse.caminatas.dto.CaminataDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminataCaminanteService;

@RestController
@RequestMapping("/caminatas")
public class CaminataCaminanteController 
{
    @Autowired
    private CaminataCaminanteService caminataCaminanteService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Asocia un caminante existente a una caminata
     */
    @PostMapping(value = "/{caminataId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO addCaminante(@PathVariable Long caminataId, @PathVariable Long caminanteId) throws EntityNotFoundException
    {
        CaminataEntity caminata = caminataCaminanteService.addCaminante(caminataId, caminanteId);
        return modelMapper.map(caminata, CaminataDetailDTO.class);
    }

    /*
     * Busca y devuelve un caminante específico asociado a una caminata.
     */
    @GetMapping(value = "/{caminataId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminanteDetailDTO getCaminante(@PathVariable Long caminataId, @PathVariable Long caminanteId) throws EntityNotFoundException, IllegalOperationException
    {
        CaminanteEntity caminante = caminataCaminanteService.getCaminante(caminataId, caminanteId);
        return modelMapper.map(caminante, CaminanteDetailDTO.class);
    }

    /*
     * Actualiza la lista de caminantes de una caminata con la lista que se recibe en el cuerpo. 
     */
    @PutMapping(value = "/{caminataId}/caminantes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> replaceCaminantes(@PathVariable Long caminataId, @RequestBody List<CaminanteDTO> caminantes) throws EntityNotFoundException
    {
        List<CaminanteEntity> entities = modelMapper.map(caminantes, new TypeToken<List<CaminanteEntity>>() {}.getType());
        List<CaminanteEntity> listaCaminantes = caminataCaminanteService.replaceCaminantes(caminataId, entities);
        return modelMapper.map(listaCaminantes, new TypeToken<List<CaminanteDetailDTO>>() {}.getType());
    }

    /*
     * Busca y devuelve todos los caminantes asociados a una caminata. 
     */
    @GetMapping(value = "/{caminataId}/caminantes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> getCaminantes(@PathVariable Long caminataId) throws EntityNotFoundException
    {
        List<CaminanteEntity> caminantes = caminataCaminanteService.listCaminantes(caminataId);
        return modelMapper.map(caminantes, new TypeToken<List<CaminanteDetailDTO>>() {}.getType());
    }

    /*
     * Elimina la conexión entre el caminante y la caminata recibidos en la URL.
     */
    @DeleteMapping(value = "/{caminataId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCaminante(@PathVariable Long caminataId, @PathVariable Long caminanteId) throws EntityNotFoundException
    {
        caminataCaminanteService.removeCaminante(caminataId, caminanteId);
    }
}