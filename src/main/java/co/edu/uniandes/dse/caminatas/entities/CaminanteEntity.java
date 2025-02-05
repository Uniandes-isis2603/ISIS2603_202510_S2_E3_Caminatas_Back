package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;

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
}
