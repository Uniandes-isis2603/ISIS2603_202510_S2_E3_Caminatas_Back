/*package co.edu.uniandes.dse.caminatas.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.CaminanteDetailDTO;
import co.edu.uniandes.dse.caminatas.dto.BlogDTO;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.services.CaminanteBlogCrearService;
import co.edu.uniandes.dse.caminatas.services.CaminanteBlogInteractuaService;

@RestController
@RequestMapping("/caminantes")
public class CaminanteBlogController {

    @Autowired
    private CaminanteBlogCrearService caminanteBlogService;

    @Autowired
    private ModelMapper modelMapper;
    
    //Asigna o reemplaza la instancia de blog de un caminante.
    @PutMapping(value = "/{caminanteId}/blog/{blogId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminanteDetailDTO replaceBlog(@PathVariable Long caminanteId, @RequestBody BlogDTO blog) throws EntityNotFoundException {
        CaminanteEntity caminante = caminanteBlogService.replaceBlog(caminanteId, blog.getId());
        return modelMapper.map(caminante, CaminanteDetailDTO.class);
    }
}*/