package co.edu.uniandes.dse.caminatas.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class PatrocinadorEntity extends BaseEntity {

    private String nombre;
    private String documento;
    private String correo;
    private String telefono;

    @PodamExclude
    @OneToMany(mappedBy = "patrocinador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaminataCompetenciaEntity> caminatasCompetencia = new ArrayList<>();
}
