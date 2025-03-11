package co.edu.uniandes.dse.caminatas.dto;

import lombok.Data;
@Data
public class CaminanteDTO {
    private Long id;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private String direccion; 
    private boolean experienciaPrevia;
    private boolean tratamientosMed;
    private boolean lesion;
    private boolean problemasRes;
}
