package co.edu.uniandes.dse.caminatas.entities;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Data
@Entity
public class CaminanteEntity extends BaseEntity
{
    private String nombre;
    private int documento;
    private String correo;
    private String telefono;
    private String direccion; 
    private boolean experienciaPrevia;
    private boolean tratamientosMed;
    private boolean lesion;
    private boolean problemasRes;
    
    @OneToMany(mappedBy = "caminante")
    private List<PagoEntity> pagos;

    @ManyToMany
    private List<BlogEntity> blogsInteractuados;
    
    @OneToMany(mappedBy = "caminante")
    private List<BlogEntity> blogsCreados;
}

