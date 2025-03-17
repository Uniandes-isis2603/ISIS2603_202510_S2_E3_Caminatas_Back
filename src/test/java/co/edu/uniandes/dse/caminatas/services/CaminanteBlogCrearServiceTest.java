package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.entities.CaminanteEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.BlogRepository;
import co.edu.uniandes.dse.caminatas.repositories.CaminanteRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
public class CaminanteBlogCrearServiceTest {

    @InjectMocks
    private CaminanteBlogCrearService caminanteBlogCrearService;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private CaminanteRepository caminanteRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private CaminanteEntity caminante;
    private BlogEntity blog;

    @BeforeEach
    void setUp() {
        caminante = factory.manufacturePojo(CaminanteEntity.class);
        blog = factory.manufacturePojo(BlogEntity.class);
        blog.setCaminante(caminante);
    }

    @Test
    void testAddBlogSuccess() throws EntityNotFoundException {
        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));
        when(blogRepository.findById(blog.getId())).thenReturn(Optional.of(blog));

        BlogEntity result = caminanteBlogCrearService.addBlog(caminante.getId(), blog.getId());

        assertEquals(blog.getId(), result.getId());
        assertEquals(caminante, result.getCaminante());
    }

    @Test
    void testAddBlogCaminanteNotFound() {
        when(caminanteRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogCrearService.addBlog(1L, blog.getId());
        });
    }

    @Test
    void testAddBlogBlogNotFound() {
        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogCrearService.addBlog(caminante.getId(), 1L);
        });
    }

    @Test
    void testGetBlogsSuccess() throws EntityNotFoundException {
        List<BlogEntity> blogs = new ArrayList<>();
        blogs.add(blog);
        caminante.setBlogsCreados(blogs);

        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));

        List<BlogEntity> result = caminanteBlogCrearService.getBlogs(caminante.getId());
        assertEquals(1, result.size());
        assertEquals(blog.getId(), result.get(0).getId());
    }

    @Test
    void testRemoveBlogSuccess() throws EntityNotFoundException {
        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));
        when(blogRepository.findById(blog.getId())).thenReturn(Optional.of(blog));

        assertDoesNotThrow(() -> caminanteBlogCrearService.removeBlog(caminante.getId(), blog.getId()));
        assertNull(blog.getCaminante());
    }

    @Test
    void testRemoveBlogNotFound() {
        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            caminanteBlogCrearService.removeBlog(caminante.getId(), 1L);
        });
    }

    @Test
    void testGetBlogNotAssociated() {
        CaminanteEntity otroCaminante = factory.manufacturePojo(CaminanteEntity.class);
        blog.setCaminante(otroCaminante);

        when(caminanteRepository.findById(caminante.getId())).thenReturn(Optional.of(caminante));
        when(blogRepository.findById(blog.getId())).thenReturn(Optional.of(blog));

        assertThrows(IllegalOperationException.class, () -> {
            caminanteBlogCrearService.getBlog(caminante.getId(), blog.getId());
        });
    }
}
