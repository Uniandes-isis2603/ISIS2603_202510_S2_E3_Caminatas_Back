package co.edu.uniandes.dse.caminatas.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.uniandes.dse.caminatas.podam.DateStrategy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Entity
public class CaminataEntity extends BaseEntity
{
    private int numero;
    private String titulo;
    private String tipo;

    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date fecha;
    
    private LocalTime hora;
    private String departamento;
    private String ciudad;
    private float duracionEstimadaMinutos;
    

    @PodamExclude
    @OneToMany(mappedBy = "caminata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoEntity> pagos = new ArrayList<>();

    @PodamExclude
    @ManyToMany(mappedBy = "caminatas")
    private List<CaminanteEntity> caminantes = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private EmpresaEntity empresa;

    @PodamExclude
    @OneToOne
    private SeguroEntity seguro;

}
