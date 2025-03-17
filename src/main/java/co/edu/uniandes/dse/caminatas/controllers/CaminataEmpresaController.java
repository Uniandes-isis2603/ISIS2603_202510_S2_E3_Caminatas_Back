package co.edu.uniandes.dse.caminatas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.CaminataDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.EmpresaDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.services.CaminataEmpresaService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/caminatas")
public class CaminataEmpresaController 
{
    @Autowired
    private CaminataEmpresaService caminataEmpresaService;

    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping(value = "/{caminataId}/empresas/{empresaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO addEmpresa(@PathVariable Long caminataId, @PathVariable Long empresaId) throws EntityNotFoundException
    {
        CaminataEntity caminata = caminataEmpresaService.addEmpresa(caminataId, empresaId);
        return modelMapper.map(caminata, CaminataDetailDTO.class);
    }
    
    @GetMapping("/{caminataId}/empresas/{empresaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmpresaDetailDTO getEmpresa(@PathVariable Long caminataId, @PathVariable Long empresaId) throws EntityNotFoundException
    {
        EmpresaEntity caminante = caminataEmpresaService.getEmpresa(caminataId, empresaId);
        return modelMapper.map(caminante, EmpresaDetailDTO.class);
    }
    
}
