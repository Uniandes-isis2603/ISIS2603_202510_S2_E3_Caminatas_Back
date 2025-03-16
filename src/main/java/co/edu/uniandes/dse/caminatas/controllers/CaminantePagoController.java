package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.CaminanteDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminanteDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.PagoDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminantePagoService;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/caminantes")
public class CaminantePagoController {

    @Autowired
    private CaminantePagoService caminantePagoService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{caminanteID}/pagos/{pagoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public CaminanteDetailDTO addPago(@PathVariable Long caminanteID, @PathVariable Long pagoID) throws EntityNotFoundException, IllegalOperationException 
    {
		CaminanteEntity caminante = caminantePagoService.addPago(caminanteID, pagoID);
		return modelMapper.map(caminante, CaminanteDetailDTO.class);
	}

    @GetMapping(value = "/{caminanteID}/pagos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PagoDetailDTO> getPagos(@PathVariable Long caminanteID) throws EntityNotFoundException,  IllegalOperationException  {
		List<PagoEntity> pagos = caminantePagoService.listPagos(caminanteID);
		return modelMapper.map(pagos, new TypeToken<List<PagoDetailDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{caminanteID}/pagos/{pagoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public PagoDetailDTO getPago(@PathVariable Long caminanteID, @PathVariable Long pagoID) throws EntityNotFoundException,  IllegalOperationException  {
		PagoEntity pagos = caminantePagoService.getPago(caminanteID, pagoID);
		return modelMapper.map(pagos, PagoDetailDTO.class);
	}
}
