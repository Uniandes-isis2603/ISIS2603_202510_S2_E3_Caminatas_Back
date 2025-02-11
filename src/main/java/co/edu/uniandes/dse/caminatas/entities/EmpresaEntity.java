package co.edu.uniandes.dse.caminatas.entities;

import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class EmpresaEntity 
{
    private Long id;
    private String nombre;
    private int documento;
    private String correo;

    @OneToMany(mappedBy = "empresa")
    private List<PagoEntity> pagos;

}
