package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data

public class SeguroEntity extends BaseEntity {
    private Long numero;
    private String nombre;
    private String tipo;
    private String cobertura;
    private String condiciones;
    private float costo;
    
    @PodamExclude
    @OneToOne
    private CaminataEntity caminata;

}
