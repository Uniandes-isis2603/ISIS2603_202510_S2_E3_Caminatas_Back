package co.edu.uniandes.dse.caminatas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.CaminataDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.EmpresaDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminataEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.services.CaminataEmpresaService;

@RestController
@RequestMapping("/caminatas")
public class CaminataEmpresaController 
{
    @Autowired
    private CaminataEmpresaService caminataEmpresaService;

    @Autowired
    private ModelMapper modelMapper;
    
    /*
     * Reemplaza la instacia de empresa de una caminata
     */
    @PutMapping(value = "/{caminataId}/empresa/{empresaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminataDetailDTO replaceEmpresa(@PathVariable Long caminataId, @RequestBody EmpresaDTO empresa) throws EntityNotFoundException
    {
       CaminataEntity caminata = caminataEmpresaService.replaceEmpresa(caminataId, empresa.getId());
       return modelMapper.map(caminata, CaminataDetailDTO.class);
    }

}
