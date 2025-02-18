package co.edu.uniandes.dse.caminatas.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.caminatas.entities.BlogEntity;
import co.edu.uniandes.dse.caminatas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.caminatas.exceptions.IllegalOperationException;

import java.util.List;

@DataJpaTest
@Import(BlogService.class)
class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TestEntityManager entityManager;

    private BlogEntity testBlog;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from BlogEntity").executeUpdate();
    }

    private void insertData() {
        testBlog = new BlogEntity();
        testBlog.setTitulo("Test Blog");
        testBlog.setText("This is a test blog content.");
        entityManager.persist(testBlog);
    }

    @Test
    void testCreateBlog() throws IllegalOperationException {
        BlogEntity newBlog = new BlogEntity();
        newBlog.setTitulo("New Blog");
        newBlog.setText("This is a new blog content.");

        BlogEntity result = blogService.createBlog(newBlog);
        assertNotNull(result);
        assertEquals(newBlog.getTitulo(), result.getTitulo());
        assertEquals(newBlog.getText(), result.getText());
    }

    @Test
    void testGetBlog() throws EntityNotFoundException {
        BlogEntity result = blogService.getBlog(testBlog.getId());
        assertNotNull(result);
        assertEquals(testBlog.getId(), result.getId());
        assertEquals(testBlog.getTitulo(), result.getTitulo());
        assertEquals(testBlog.getText(), result.getText());
    }

    @Test
    void testGetBlogs() {
        List<BlogEntity> list = blogService.getBlogs();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateBlog() throws EntityNotFoundException, IllegalOperationException {
        BlogEntity updatedBlog = new BlogEntity();
        updatedBlog.setTitulo("Updated Blog");
        updatedBlog.setText("This is an updated blog content.");

        BlogEntity result = blogService.updateBlog(testBlog.getId(), updatedBlog);
        assertNotNull(result);
        assertEquals(updatedBlog.getTitulo(), result.getTitulo());
        assertEquals(updatedBlog.getText(), result.getText());
    }

    @Test
    void testDeleteBlog() throws EntityNotFoundException {
        blogService.deleteBlog(testBlog.getId());
        BlogEntity deleted = entityManager.find(BlogEntity.class, testBlog.getId());
        assertNull(deleted);
    }

    @Test
    void testCreateBlogWithNullTitle() {
        BlogEntity newBlog = new BlogEntity();
        newBlog.setTitulo(null);
        newBlog.setText("This is a test content.");

        assertThrows(IllegalOperationException.class, () -> {
            blogService.createBlog(newBlog);
        });
    }

    @Test
    void testCreateBlogWithNullText() {
        BlogEntity newBlog = new BlogEntity();
        newBlog.setTitulo("Test Blog");
        newBlog.setText(null);

        assertThrows(IllegalOperationException.class, () -> {
            blogService.createBlog(newBlog);
        });
    }

    @Test
    void testGetInvalidBlog() {
        assertThrows(EntityNotFoundException.class, () -> {
            blogService.getBlog(0L);
        });
    }
}

