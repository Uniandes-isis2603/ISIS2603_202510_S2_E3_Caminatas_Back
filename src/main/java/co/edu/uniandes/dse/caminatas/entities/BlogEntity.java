package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.io.File;

@Entity
@Data
public class BlogEntity extends BaseEntity {
    private String titulo;
    private File foto;
    private File video;
    private String text;

    @ManyToOne
    private CaminanteEntity caminante;
}
