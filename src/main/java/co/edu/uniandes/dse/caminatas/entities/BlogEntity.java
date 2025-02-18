package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class BlogEntity extends BaseEntity {
    private String titulo;
    private File foto;
    private File video;
    private String text;

    @PodamExclude
    @ManyToOne
    private CaminanteEntity caminante;

    @PodamExclude
    @ManyToMany(mappedBy = "blogsInteractuados")
    private List<CaminanteEntity> caminantesInteracciones = new ArrayList<>();
}
