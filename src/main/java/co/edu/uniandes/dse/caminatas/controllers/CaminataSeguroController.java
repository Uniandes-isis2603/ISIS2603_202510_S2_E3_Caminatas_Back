package co.edu.uniandes.dse.caminatas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.SeguroDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.SeguroEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminataSeguroService;

@RestController
@RequestMapping("/caminatas")
public class CaminataSeguroController 
{
    @Autowired
    private CaminataSeguroService caminataSeguroService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Asocia un seguro a una caminata
     */
    @PostMapping(value = "/{caminataId}/seguros/{seguroId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SeguroDetailDTO addSeguro(@PathVariable Long caminataId, @PathVariable Long seguroId) throws EntityNotFoundException
    {
        SeguroEntity seguro = caminataSeguroService.addSeguro(caminataId, seguroId);
        return modelMapper.map(seguro, SeguroDetailDTO.class);
    }

    /*
     * Obtiene el seguro asociado a una caminata
     */
    @GetMapping("/{caminataId}/seguros/{seguroId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SeguroDetailDTO getSeguro(@PathVariable Long caminataId, @PathVariable Long seguroId) throws EntityNotFoundException, IllegalOperationException
    {
        SeguroEntity seguro = caminataSeguroService.getSeguro(caminataId, seguroId);
        return modelMapper.map(seguro, SeguroDetailDTO.class);
    }

    /*
     * Desasocia un seguro de una caminata
     */
    @PostMapping(value = "/{caminataId}/seguros/{seguroId}/remove")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeSeguro(@PathVariable Long caminataId, @PathVariable Long seguroId) throws EntityNotFoundException
    {
        caminataSeguroService.removeSeguro(caminataId, seguroId);
    }
}
