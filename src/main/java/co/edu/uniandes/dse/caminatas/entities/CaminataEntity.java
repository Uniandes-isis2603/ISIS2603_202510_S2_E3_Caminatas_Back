package co.edu.uniandes.dse.caminatas.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class CaminataEntity extends BaseEntity
{
    private int numero;
    private String titulo;
    private String tipo;
    private Date fecha;
    private LocalTime hora;
    private String departamento;
    private String ciudad;
    private float duracionEstimadaMinutos;
    

    @OneToMany(mappedBy = "caminata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoEntity> pagos = new ArrayList<>();

    @ManyToMany(mappedBy = "caminatas")
    private List<CaminanteEntity> caminantes = new ArrayList<>();

    @ManyToOne
    private EmpresaEntity empresa;

    @OneToOne
    private SeguroEntity seguro;

}
