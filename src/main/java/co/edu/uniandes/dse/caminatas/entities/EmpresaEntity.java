package co.edu.uniandes.dse.caminatas.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class EmpresaEntity extends BaseEntity {

    private String nombre;
    private int documento;
    private String correo;

    @PodamExclude
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoEntity> pagos = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaminataEntity> caminatas = new ArrayList<>();

}
