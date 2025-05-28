package co.edu.uniandes.dse.caminatas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.caminatas.repositories.BlogRepository;

@Service
public class BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Transactional
    public BlogEntity createBlog(BlogEntity blog) throws IllegalOperationException {
        validateBlog(blog);
        return blogRepository.save(blog);
    }

    @Transactional(readOnly = true)
    public List<BlogEntity> getBlogs() {
        return blogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BlogEntity getBlog(Long blogId) throws EntityNotFoundException {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog no encontrado"));
    }

    @Transactional
    public BlogEntity updateBlog(Long blogId, BlogEntity blog) throws EntityNotFoundException, IllegalOperationException {
        BlogEntity existingBlog = getBlog(blogId);
        validateBlog(blog);
        blog.setId(existingBlog.getId());
        return blogRepository.save(blog);
    }

    @Transactional
    public void deleteBlog(Long blogId) throws EntityNotFoundException {
        if (!blogRepository.existsById(blogId)) {
            throw new EntityNotFoundException("Blog no encontrado");
        }
        blogRepository.deleteById(blogId);
    }

    private void validateBlog(BlogEntity blog) throws IllegalOperationException {
        if (blog.getTitulo() == null || blog.getTitulo().isEmpty()) {
            throw new IllegalOperationException("El título del blog no puede ser vacío");
        }
        if (blog.getText() == null || blog.getText().isEmpty()) {
            throw new IllegalOperationException("El texto del blog no puede ser vacío");
        }
    }
}
