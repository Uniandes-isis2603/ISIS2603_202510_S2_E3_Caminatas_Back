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

    @Transactional
    public BlogEntity addBlog(Long caminanteId, Long blogId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar un blog al caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        Optional<BlogEntity> blogEntity = blogRepository.findById(blogId);

        if (caminanteEntity.isEmpty())
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");

        if (blogEntity.isEmpty())
            throw new EntityNotFoundException("El blog con id = " + blogId + " no existe.");

        blogEntity.get().setCaminante(caminanteEntity.get());
        log.info("Termina proceso de asociar un blog al caminante con id = {}", caminanteId);
        return blogEntity.get();
    }

    @Transactional
    public List<BlogEntity> getBlogs(Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los blogs del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        if (caminanteEntity.isEmpty())
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");

        log.info("Termina proceso de consultar todos los blogs del caminante con id = {}", caminanteId);
        return caminanteEntity.get().getBlogsCreados();
    }

    @Transactional
    public BlogEntity getBlog(Long caminanteId, Long blogId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el blog con id = {} del caminante con id = {}", blogId, caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        Optional<BlogEntity> blogEntity = blogRepository.findById(blogId);

        if (caminanteEntity.isEmpty())
        throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");

        if (blogEntity.isEmpty())
            throw new EntityNotFoundException("El blog con id = " + blogId + " no existe.");

        if (!blogEntity.get().getCaminante().equals(caminanteEntity.get()))
            throw new IllegalOperationException("El blog no está asociado al caminante");

        log.info("Termina proceso de consultar el blog con id = {} del caminante con id = {}", blogId, caminanteId);
        return blogEntity.get();
    }

    @Transactional
    public List<BlogEntity> addBlogs(Long caminanteId, List<BlogEntity> blogs) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los blogs asociados al caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        if (caminanteEntity.isEmpty())
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");

        for (BlogEntity blog : blogs) {
            Optional<BlogEntity> blogEntity = blogRepository.findById(blog.getId());
            if (blogEntity.isEmpty())
                throw new EntityNotFoundException("El blog con id = " + blog.getId() + " no existe.");
        }

        caminanteEntity.get().setBlogsCreados(blogs);
        log.info("Finaliza proceso de reemplazar los blogs asociados al caminante con id = {}", caminanteId);
        return caminanteEntity.get().getBlogsCreados();
    }

    @Transactional
    public void removeBlog(Long caminanteId, Long blogId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un blog del caminante con id = {}", caminanteId);
        Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminanteId);
        Optional<BlogEntity> blogEntity = blogRepository.findById(blogId);

        if (caminanteEntity.isEmpty())
            throw new EntityNotFoundException("El caminante con id = " + caminanteId + " no existe.");

        if (blogEntity.isEmpty())
        throw new EntityNotFoundException("El blog con id = " + blogId + " no existe.");

        blogEntity.get().setCaminante(null);
        caminanteEntity.get().getBlogsCreados().remove(blogEntity.get());
        log.info("Finaliza proceso de borrar un blog del caminante con id = {}", caminanteId);
    }
}
