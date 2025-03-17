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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.EmpresaDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.PagoDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.EmpresaEntity;
import co.edu.uniandes.dse.caminatas.entities.PagoEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.EmpresaPagoService;
import co.edu.uniandes.dse.caminatas.services.EmpresaService;

/**
 * Controlador REST para la relación Empresa - Pago
 */
@RestController
@RequestMapping("/empresas")
public class EmpresaPagoController {
    
    @Autowired
    private EmpresaPagoService empresaPagoService;
    
    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia un pago existente a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @return DTO de la empresa con el pago asociado
     * @throws EntityNotFoundException Si la empresa o el pago no existen
     * @throws IllegalOperationException Si el pago ya está asociado a un caminante
     */
    @PostMapping(value = "/{empresaId}/pagos/{pagoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmpresaDetailDTO addPagoToEmpresa(@PathVariable Long empresaId, @PathVariable Long pagoId) 
            throws EntityNotFoundException, IllegalOperationException {
        // Asociar el pago a la empresa
        empresaPagoService.addPagoToEmpresa(empresaId, pagoId);
        // Obtener la empresa actualizada
        EmpresaEntity empresaEntity = empresaService.getEmpresa(empresaId);
        return modelMapper.map(empresaEntity, EmpresaDetailDTO.class);
    }

    /**
     * Obtiene un pago específico asociado a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @return DTO del pago consultado
     * @throws EntityNotFoundException Si la empresa o el pago no existen o si el pago no está asociado a la empresa
     */
    @GetMapping("/{empresaId}/pagos/{pagoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PagoDetailDTO getPagoFromEmpresa(@PathVariable Long empresaId, @PathVariable Long pagoId) 
            throws EntityNotFoundException {
        PagoEntity pagoEntity = empresaPagoService.getPagoFromEmpresa(empresaId, pagoId);
        return modelMapper.map(pagoEntity, PagoDetailDTO.class);
    }

    /**
     * Obtiene todos los pagos asociados a una empresa
     *
     * @param empresaId Identificador de la empresa
     * @return Lista de DTOs de los pagos asociados
     * @throws EntityNotFoundException Si la empresa no existe
     */
    @GetMapping("/{empresaId}/pagos")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PagoDetailDTO> getPagosFromEmpresa(@PathVariable Long empresaId) 
            throws EntityNotFoundException {
        List<PagoEntity> pagos = empresaPagoService.getPagosFromEmpresa(empresaId);
        return modelMapper.map(pagos, new TypeToken<List<PagoDetailDTO>>() {}.getType());
    }

    /**
     * Desasocia un pago de una empresa
     *
     * @param empresaId Identificador de la empresa
     * @param pagoId Identificador del pago
     * @return DTO de la empresa actualizada
     * @throws EntityNotFoundException Si la empresa o el pago no existen o si el pago no está asociado a la empresa
     */
    @DeleteMapping(value = "/{empresaId}/pagos/{pagoId}")
    @ResponseStatus(code = HttpStatus.OK)
    public EmpresaDetailDTO removePagoFromEmpresa(@PathVariable Long empresaId, @PathVariable Long pagoId) 
            throws EntityNotFoundException {
        // Desasociar el pago de la empresa
        empresaPagoService.removePagoFromEmpresa(empresaId, pagoId);
        // Obtener la empresa actualizada
        EmpresaEntity empresaEntity = empresaService.getEmpresa(empresaId);
        return modelMapper.map(empresaEntity, EmpresaDetailDTO.class);
    }
}