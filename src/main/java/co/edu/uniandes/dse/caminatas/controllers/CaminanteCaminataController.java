package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.caminatas.dto.CaminataDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminataDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminanteCaminataService;

@RestController
@RequestMapping("/caminantes")
public class CaminanteCaminataController {
    
    @Autowired
    private CaminanteCaminataService caminanteCaminataService;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{caminanteId}/caminatas/{caminataId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO addCaminata(@PathVariable Long caminanteId, @PathVariable Long caminataId) throws EntityNotFoundException {
        CaminanteEntity caminata = caminanteCaminataService.addCaminata(caminanteId, caminataId);
        return modelMapper.map(caminata, CaminataDetailDTO.class);
    }

    @GetMapping(value = "/{caminanteId}/caminatas/{caminataId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO getCaminata(@PathVariable Long caminanteId, @PathVariable Long caminataId) throws EntityNotFoundException, IllegalOperationException {
        CaminataEntity caminata = caminanteCaminataService.getCaminata(caminanteId, caminataId);
        return modelMapper.map(caminata, CaminataDetailDTO.class);
    }

    @GetMapping(value = "/{caminanteId}/caminatas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminataDetailDTO> getCaminatas(@PathVariable Long caminanteId) throws EntityNotFoundException {
        List<CaminataEntity> caminatas = caminanteCaminataService.listCaminatas(caminanteId);
        return modelMapper.map(caminatas, new TypeToken<List<CaminataDetailDTO>>() {}.getType());
    }

    @PutMapping(value = "/{caminanteId}/caminatas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminataDetailDTO> replaceCaminatas(@PathVariable Long caminanteId, @RequestBody List<CaminataDTO> caminatas) throws EntityNotFoundException {
        List<CaminataEntity> entities = modelMapper.map(caminatas, new TypeToken<List<CaminataEntity>>() {}.getType());
        List<CaminataEntity> listaCaminatas = caminanteCaminataService.replaceCaminatas(caminanteId, entities);
        return modelMapper.map(listaCaminatas, new TypeToken<List<CaminataDetailDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{caminanteId}/caminatas/{caminataId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCaminata(@PathVariable Long caminanteId, @PathVariable Long caminataId) throws EntityNotFoundException {
        caminanteCaminataService.removeCaminata(caminanteId, caminataId);
    }
}
