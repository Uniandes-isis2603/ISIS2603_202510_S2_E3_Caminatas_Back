package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.CaminanteDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.services.CaminanteService;

@RestController
@RequestMapping("/caminantes")
public class CaminanteController {

    @Autowired
    private CaminanteService caminanteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> findAll() {
        List<CaminanteEntity> caminantes = caminanteService.getCaminantes();
        return modelMapper.map(caminantes, new TypeToken<List<CaminanteDetailDTO>>() {
        }.getType());
    }
    
}
