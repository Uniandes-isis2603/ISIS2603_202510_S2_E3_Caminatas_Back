package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import scala.reflect.io.File;

@Entity
@Data

public class BlogEntity extends BaseEntity{
    private File foto;
    private File video;
    private String text;
    // private Caminante caminante;

}
