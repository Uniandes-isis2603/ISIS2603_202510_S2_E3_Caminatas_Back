package co.edu.uniandes.dse.caminatas.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class PatrocinadorEntity {

    private Long id;
    private String nombre;
    private int documento;
    private String correo;
    private String telefono;

    @OneToMany(mappedBy = "patrocinador")
    private List<CaminataEntity> caminatas;
}
