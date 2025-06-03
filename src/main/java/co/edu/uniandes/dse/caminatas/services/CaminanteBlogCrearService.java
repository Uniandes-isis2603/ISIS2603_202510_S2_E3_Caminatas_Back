package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.BlogRepository;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaminanteBlogCrearService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CaminanteRepository caminanteRepository;

    private CaminanteEntity getCaminanteOrThrow(Long caminanteId) throws EntityNotFoundException {
    return caminanteRepository.findById(caminanteId)
            .orElseThrow(() -> new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe."));
    }

    private BlogEntity getBlogOrThrow(Long blogId) throws EntityNotFoundException {
        return blogRepository.findById(blogId)
            .orElseThrow(() -> new EntityNotFoundException("El blog con id = " + blogId + " no existe."));
    }

    @Transactional
    public BlogEntity addBlog(Long caminanteId, Long blogId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar un blog al caminante con id = {}", caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);
        BlogEntity blog = getBlogOrThrow(blogId);
        blog.setCaminante(caminante);
        log.info("Termina proceso de asociar un blog al caminante con id = {}", caminanteId);
        return blog;
    }

    @Transactional
    public List<BlogEntity> getBlogs(Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los blogs del caminante con id = {}", caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);
        log.info("Termina proceso de consultar todos los blogs del caminante con id = {}", caminanteId);
        return caminante.getBlogsCreados();
    }

    @Transactional
    public BlogEntity getBlog(Long caminanteId, Long blogId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el blog con id = {} del caminante con id = {}", blogId, caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);
        BlogEntity blog = getBlogOrThrow(blogId);

        if (!blog.getCaminante().equals(caminante))
            throw new IllegalOperationException("El blog no está asociado al caminante");

        log.info("Termina proceso de consultar el blog con id = {} del caminante con id = {}", blogId, caminanteId);
        return blog;
    }

    @Transactional
    public List<BlogEntity> addBlogs(Long caminanteId, List<BlogEntity> blogs) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los blogs asociados al caminante con id = {}", caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);

        for (BlogEntity blog : blogs) {
            getBlogOrThrow(blog.getId());
        }

        caminante.setBlogsCreados(blogs);
        log.info("Finaliza proceso de reemplazar los blogs asociados al caminante con id = {}", caminanteId);
        return caminante.getBlogsCreados();
    }

   @Transactional
    public List<BlogEntity> replaceBlogs(Long caminanteId, List<BlogEntity> blogs) throws EntityNotFoundException {
        log.info("Inicia proceso de actualizar el caminante con id = {}", caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);

        for (BlogEntity blog : blogs) {
            BlogEntity b = getBlogOrThrow(blog.getId());
            b.setCaminante(caminante);
        }
        return blogs;
    }

    @Transactional
    public void removeBlog(Long caminanteId, Long blogId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un blog del caminante con id = {}", caminanteId);
        CaminanteEntity caminante = getCaminanteOrThrow(caminanteId);
        BlogEntity blog = getBlogOrThrow(blogId);

        blog.setCaminante(null);
        caminante.getBlogsCreados().remove(blog);
        log.info("Finaliza proceso de borrar un blog del caminante con id = {}", caminanteId);
    }
}
