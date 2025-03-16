package co.edu.uniandes.dse.caminatas.dto;
import lombok.Data;

@Data
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private String documento;
    private String correo;
}
