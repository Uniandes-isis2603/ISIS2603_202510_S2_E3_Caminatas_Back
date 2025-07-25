package co.edu.uniandes.dse.caminatas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.BlogRepository;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j; 

@Slf4j
@Service
public class CaminanteBlogInteractuaService {

    private static final String MENSAJE_1 = "El blog con id = ";
    private static final String MENSAJE_2 = " no existe.";
    private static final String MENSAJE_3 = "El caminante con id = ";

    @Autowired
    private BlogRepository blogRepository;

    @Autowired 
    private CaminanteRepository caminanteRepository;

    @Transactional
    public BlogEntity addCaminante(Long blogId, Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de agregar un caminante al blog con id = {}", blogId);
        Optional<BlogEntity> blog = blogRepository.findById(blogId);
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        
        if (blog.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + blogId + MENSAJE_2);
        }
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminanteId + MENSAJE_2);
        }
        blog.get().getCaminantesInteracciones().add(caminante.get());
        log.info("Termina proceso de agregar un caminante al blog con id = {}", blogId);
        return blog.get();
    }

    @Transactional  
    public List<CaminanteEntity> listCaminantes(Long blogId) throws EntityNotFoundException {
        log.info("Inicia proceso de obtener los caminantes del blog con id = {}", blogId);
        Optional<BlogEntity> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + blogId + MENSAJE_2);
        }
        log.info("Termina proceso de obtener los caminantes del blog con id = {}", blogId);
        return blog.get().getCaminantesInteracciones();
    }

    @Transactional
    public CaminanteEntity getCaminante(Long blogId, Long caminanteId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de obtener un caminante del blog con id = {}", blogId);
        Optional<BlogEntity> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + blogId + MENSAJE_2);
        }
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminanteId + MENSAJE_2);
        }
        if (!blog.get().getCaminantesInteracciones().contains(caminante.get())) {
            throw new IllegalOperationException(MENSAJE_3 + caminanteId + " no está asociado al blog con id = " + blogId);
        }
        log.info("Termina proceso de obtener un caminante del blog con id = {}", blogId);
        return caminante.get();
    }

    @Transactional
    public List<CaminanteEntity> replaceCaminantes(Long blogId, List<CaminanteEntity> caminantes) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los caminantes del blog con id = {}", blogId);
        Optional<BlogEntity> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + blogId + MENSAJE_2);
        }
        for (CaminanteEntity caminante : caminantes) {
            Optional<CaminanteEntity> caminanteEntity = caminanteRepository.findById(caminante.getId());
            if (caminanteEntity.isEmpty()) {
                throw new EntityNotFoundException(MENSAJE_3 + caminante.getId() + MENSAJE_2);
            }
            if (!blog.get().getCaminantesInteracciones().contains(caminanteEntity.get())) {
                blog.get().getCaminantesInteracciones().add(caminanteEntity.get());
            }
        }
        log.info("Termina proceso de reemplazar los caminantes del blog con id = {}", blogId);
        return blog.get().getCaminantesInteracciones();
    }

    @Transactional
    public void removeCaminante(Long blogId, Long caminanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar un caminante del blog con id = {}", blogId);
        Optional<BlogEntity> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_1 + blogId + MENSAJE_2);
        }
        Optional<CaminanteEntity> caminante = caminanteRepository.findById(caminanteId);
        if (caminante.isEmpty()) {
            throw new EntityNotFoundException(MENSAJE_3 + caminanteId + MENSAJE_2);
        }
        blog.get().getCaminantesInteracciones().remove(caminante.get());
        log.info("Termina proceso de eliminar un caminante del blog con id = {}", blogId);   
    }
    
}