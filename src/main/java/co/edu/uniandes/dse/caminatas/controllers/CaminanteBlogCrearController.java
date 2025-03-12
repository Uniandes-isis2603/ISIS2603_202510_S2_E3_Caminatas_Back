package co.edu.uniandes.dse.caminatas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.CaminanteBlogCrearService;

@RestController
@RequestMapping("/caminantes")
public class CaminanteBlogCrearController {

    @Autowired
    private CaminanteBlogCrearService caminanteBlogCrearService;

    /**
     * Agregar un blog a un caminante
     *
     * @param caminanteId El id del caminante al cual se va a agregar el blog.
     * @param blogId      El id del blog a agregar.
     * @return El blog creado.
     * @throws EntityNotFoundException
     */
    @PostMapping("/{caminanteId}/blogs/{blogId}")
    public ResponseEntity<BlogEntity> addBlog(@PathVariable Long caminanteId, @PathVariable Long blogId) throws EntityNotFoundException {
        BlogEntity blogEntity = caminanteBlogCrearService.addBlog(caminanteId, blogId);
        return ResponseEntity.ok(blogEntity);
    }

    /**
     * Retorna todos los blogs asociados a un caminante
     *
     * @param caminanteId El ID del caminante buscado
     * @return La lista de blogs del caminante
     * @throws EntityNotFoundException si el caminante no existe
     */
    @GetMapping("/{caminanteId}/blogs")
    public ResponseEntity<List<BlogEntity>> getBlogs(@PathVariable Long caminanteId) throws EntityNotFoundException {
        List<BlogEntity> blogs = caminanteBlogCrearService.getBlogs(caminanteId);
        return ResponseEntity.ok(blogs);
    }

    /**
     * Retorna un blog asociado a un caminante
     *
     * @param caminanteId El id del caminante a buscar.
     * @param blogId      El id del blog a buscar
     * @return El blog encontrado dentro del caminante.
     * @throws EntityNotFoundException Si el blog no se encuentra en el caminante
     * @throws IllegalOperationException Si el blog no está asociado al caminante
     */
    @GetMapping("/{caminanteId}/blogs/{blogId}")
    public ResponseEntity<BlogEntity> getBlog(@PathVariable Long caminanteId, @PathVariable Long blogId) throws EntityNotFoundException, IllegalOperationException {
        BlogEntity blogEntity = caminanteBlogCrearService.getBlog(caminanteId, blogId);
        return ResponseEntity.ok(blogEntity);
    }

    /**
     * Remplazar blogs de un caminante
     *
     * @param caminanteId El id del caminante que se quiere actualizar.
     * @param blogs       Lista de blogs que serán los del caminante.
     * @return La lista de blogs actualizada.
     * @throws EntityNotFoundException Si el caminante o un blog de la lista no se encuentran
     */
    @PutMapping("/{caminanteId}/blogs")
    public ResponseEntity<List<BlogEntity>> replaceBlogs(@PathVariable Long caminanteId, @RequestBody List<BlogEntity> blogs) throws EntityNotFoundException {
        List<BlogEntity> updatedBlogs = caminanteBlogCrearService.replaceBlogs(caminanteId, blogs);
        return ResponseEntity.ok(updatedBlogs);
    }

    /**
     * Eliminar un blog de un caminante
     *
     * @param caminanteId El id del caminante del cual se va a eliminar el blog.
     * @param blogId      El id del blog a eliminar.
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{caminanteId}/blogs/{blogId}")
    public ResponseEntity<Void> removeBlog(@PathVariable Long caminanteId, @PathVariable Long blogId) throws EntityNotFoundException {
        caminanteBlogCrearService.removeBlog(caminanteId, blogId);
        return ResponseEntity.noContent().build();
    }
}
