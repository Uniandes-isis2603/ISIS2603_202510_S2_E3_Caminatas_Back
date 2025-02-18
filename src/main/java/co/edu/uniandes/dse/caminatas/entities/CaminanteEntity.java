package co.edu.uniandes.dse.caminatas.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Data
@Entity
public class CaminanteEntity extends BaseEntity
{
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private String direccion; 
    private boolean experienciaPrevia;
    private boolean tratamientosMed;
    private boolean lesion;
    private boolean problemasRes;
    
    @PodamExclude
    @OneToMany(mappedBy = "caminante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoEntity> pagos = new ArrayList<>();

    @PodamExclude
    @ManyToMany
    private List<BlogEntity> blogsInteractuados = new ArrayList<>();
    
    @PodamExclude
    @OneToMany(mappedBy = "caminante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogEntity> blogsCreados = new ArrayList<>();

    @PodamExclude
    @ManyToMany
    private List<CaminataEntity> caminatas = new ArrayList<>();
}

