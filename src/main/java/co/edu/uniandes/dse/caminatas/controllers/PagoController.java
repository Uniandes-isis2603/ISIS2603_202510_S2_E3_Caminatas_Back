package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.PagoDTO;
import co.edu.uniandes.dse.caminatas.dto.PagoDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.PagoService;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PagoDetailDTO> findAll(){
        List<PagoEntity> pagos = pagoService.getPagos();
        return modelMapper.map(pagos, new TypeToken<List<PagoDetailDTO>>() {

        }.getType());
    }
    
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PagoDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PagoEntity pago = pagoService.getPago(id);
        return modelMapper.map(pago, PagoDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PagoDTO create(@RequestBody PagoDTO pagoDTO) throws IllegalOperationException, EntityNotFoundException {
        PagoEntity pagoEntity = pagoService.createPago(modelMapper.map(pagoDTO, PagoEntity.class));
        return modelMapper.map(pagoEntity, PagoDTO.class);
    }

}
