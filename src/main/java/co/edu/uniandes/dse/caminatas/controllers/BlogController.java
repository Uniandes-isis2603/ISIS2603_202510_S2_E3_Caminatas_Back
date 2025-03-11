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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.caminatas.dto.BlogDTO;
import co.edu.uniandes.dse.caminatas.dto.BlogDetailDTO;
import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.services.BlogService;

/**
 * Clase que implementa el recurso "blogs".
 * 
 * @author Universidad de los Andes
 */
@RestController
@RequestMapping("/blogs")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos los blogs que existen en la aplicación.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<BlogDetailDTO> findAll() {
		List<BlogEntity> blogs = blogService.getBlogs();
		return modelMapper.map(blogs, new TypeToken<List<BlogDetailDTO>>() {}.getType());
	}

	/**
	 * Busca el blog con el id asociado recibido en la URL y lo devuelve.
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BlogDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		BlogEntity blog = blogService.getBlog(id);
		return modelMapper.map(blog, BlogDetailDTO.class);
	}

	/**
	 * Crea un nuevo blog con la información que se recibe en el cuerpo de la petición.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BlogDTO create(@RequestBody BlogDTO blog) throws IllegalOperationException {
		BlogEntity blogEntity = blogService.createBlog(modelMapper.map(blog, BlogEntity.class));
		return modelMapper.map(blogEntity, BlogDTO.class);
	}

	/**
	 * Actualiza el blog con el id recibido en la URL con la información que se recibe en la petición.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public BlogDTO update(@PathVariable Long id, @RequestBody BlogDetailDTO blog) throws EntityNotFoundException, IllegalOperationException {
		BlogEntity blogEntity = blogService.updateBlog(id, modelMapper.map(blog, BlogEntity.class));
		return modelMapper.map(blogEntity, BlogDetailDTO.class);
	}

	/**
	 * Borra el blog con el id asociado recibido en la URL.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		blogService.deleteBlog(id);
	}
}