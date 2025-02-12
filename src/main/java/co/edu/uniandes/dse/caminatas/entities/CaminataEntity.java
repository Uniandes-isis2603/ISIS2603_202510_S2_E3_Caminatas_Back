package co.edu.uniandes.dse.caminatas.entities;

import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class CaminataEntity extends BaseEntity{
    private int numero;
    private String titulo;
    private String tipo;
    private Date fecha;
    private LocalTime hora;
    private String departamento;
    private String ciudad;
    private float duracionEstimadaMinutos;
    
    /*@ManyToOne
    private PatrocinadorEntity patrocinador;*/

    @OneToOne(mappedBy = "caminata", cascade = CascadeType.ALL, orphanRemoval = true)
    private PagoEntity pago;

}
