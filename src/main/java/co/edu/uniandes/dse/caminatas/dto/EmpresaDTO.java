package co.edu.uniandes.dse.caminatas.dto;
import lombok.Data;

@Data
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private int documento;
    private String correo;
}
