package co.edu.uniandes.dse.caminatas.controllers;

import co.edu.uniandes.dse.caminatas.dto.CaminanteDTO;
import co.edu.uniandes.dse.caminatas.dto.CaminanteDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminanteBlogInteractuaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class CaminanteBlogInteractuaController {

    @Autowired
    private CaminanteBlogInteractuaService caminanteBlogInteractuaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia un caminante existente con un blog existente.
     *
     * @param blogId      El ID del blog al cual se le va a asociar el caminante.
     * @param caminanteId El ID del caminante que se asocia.
     * @return {@link BlogEntity} - El blog con el caminante asociado.
     */
    @PostMapping(value = "/{blogId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BlogEntity addCaminante(@PathVariable Long blogId, @PathVariable Long caminanteId) throws EntityNotFoundException {
        return caminanteBlogInteractuaService.addCaminante(blogId, caminanteId);
    }

    /**
     * Busca y devuelve todos los caminantes que existen en un blog.
     *
     * @param blogId El ID del blog del cual se buscan los caminantes.
     * @return JSONArray {@link CaminanteDetailDTO} - Los caminantes encontrados en el blog.
     *         Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping(value = "/{blogId}/caminantes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> listCaminantes(@PathVariable Long blogId) throws EntityNotFoundException {
        List<CaminanteEntity> caminanteEntities = caminanteBlogInteractuaService.listCaminantes(blogId);
        return modelMapper.map(caminanteEntities, new TypeToken<List<CaminanteDetailDTO>>() {}.getType());
    }

    /**
     * Busca y devuelve un caminante específico de un blog.
     *
     * @param blogId      El ID del blog del cual se busca el caminante.
     * @param caminanteId El ID del caminante que se busca.
     * @return {@link CaminanteDetailDTO} - El caminante encontrado en el blog.
     */
    @GetMapping(value = "/{blogId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CaminanteDetailDTO getCaminante(@PathVariable Long blogId, @PathVariable Long caminanteId)
            throws EntityNotFoundException, IllegalOperationException {
        CaminanteEntity caminanteEntity = caminanteBlogInteractuaService.getCaminante(blogId, caminanteId);
        return modelMapper.map(caminanteEntity, CaminanteDetailDTO.class);
    }

    /**
     * Reemplaza la lista de caminantes de un blog con la lista que se recibe en el cuerpo.
     *
     * @param blogId      El ID del blog al cual se le va a asociar los caminantes.
     * @param caminantes  JSONArray {@link CaminanteDTO} - La lista de caminantes que se desea guardar.
     * @return JSONArray {@link CaminanteDetailDTO} - La lista actualizada.
     */
    @PutMapping(value = "/{blogId}/caminantes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CaminanteDetailDTO> replaceCaminantes(@PathVariable Long blogId, @RequestBody List<CaminanteDTO> caminantes)
            throws EntityNotFoundException {
        List<CaminanteEntity> caminanteEntities = modelMapper.map(caminantes, new TypeToken<List<CaminanteEntity>>() {}.getType());
        List<CaminanteEntity> updatedCaminantes = caminanteBlogInteractuaService.replaceCaminantes(blogId, caminanteEntities);
        return modelMapper.map(updatedCaminantes, new TypeToken<List<CaminanteDetailDTO>>() {}.getType());
    }

    /**
     * Elimina la conexión entre el caminante y el blog recibidos en la URL.
     *
     * @param blogId      El ID del blog al cual se le va a desasociar el caminante.
     * @param caminanteId El ID del caminante que se desasocia.
     */
    @DeleteMapping(value = "/{blogId}/caminantes/{caminanteId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCaminante(@PathVariable Long blogId, @PathVariable Long caminanteId) throws EntityNotFoundException {
        caminanteBlogInteractuaService.removeCaminante(blogId, caminanteId);
    }
}
