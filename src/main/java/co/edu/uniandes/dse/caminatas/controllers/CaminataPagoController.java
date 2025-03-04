package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.PagoDTO;
import co.edu.uniandes.dse.caminatas.dto.PagoDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminataPagoService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/caminatas")
public class CaminataPagoController 
{
    @Autowired
    private CaminataPagoService caminataPagoService;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Guarda un pago dentro de una caminata con la informacion que recibe el la
	 * URL. Se devuelve el libro que se guarda en la editorial.
     */

    @PostMapping(value =  "/{caminataId}/pago/{pagoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PagoDTO addPago(@PathVariable Long caminataId, @PathVariable("pagoId") Long pagoId) throws EntityNotFoundException
    {
        PagoEntity pagoEntity = caminataPagoService.addPago(caminataId, pagoId);
        return modelMapper.map(pagoEntity, PagoDTO.class);
    }


    /*
     * Busca y devuelve todos los pagos que existen en la caminata.
     */
    @GetMapping(value = "/{caminataId}/pagos") // <-- Cambia "pago" a "pagos"
    @ResponseStatus(code = HttpStatus.OK)
    public List<PagoDetailDTO> getPagos(@PathVariable("caminataId") Long caminataId) throws EntityNotFoundException
    {
        List<PagoEntity> pagoList = caminataPagoService.listPagos(caminataId);
        return modelMapper.map(pagoList, new TypeToken<List<PagoDetailDTO>>() {}.getType());
    }

    /*
     * Busca el pago con el id asociado dentro de la caminata con id asociado.
     */
    @GetMapping(value =  "/{caminataId}/pago/{pagoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PagoDetailDTO getPago(@PathVariable Long caminataId, @PathVariable Long pagoId) throws EntityNotFoundException, IllegalOperationException
    {
        PagoEntity pago = caminataPagoService.getPago(caminataId, pagoId);
        return modelMapper.map(pago, PagoDetailDTO.class);
    }

}
    
    

