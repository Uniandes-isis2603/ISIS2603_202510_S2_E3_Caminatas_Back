package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data

public class SeguroEntity extends BaseEntity {
    private String nombre;
    private String tipo;
    private String cobertura;
    private String condiciones;
    private float costo;
    
    @ManyToOne
    private PagoEntity pago;

}
