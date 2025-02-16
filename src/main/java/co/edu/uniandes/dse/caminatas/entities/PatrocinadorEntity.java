package co.edu.uniandes.dse.caminatas.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class PatrocinadorEntity extends BaseEntity {

    private Long id;
    private String nombre;
    private int documento;
    private String correo;
    private String telefono;

    @OneToMany(mappedBy = "patrocinador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaminataCompetenciaEntity> caminatasCompetencia = new ArrayList<>();
}
