package co.edu.uniandes.dse.caminatas.dto;

import lombok.Data;

@Data
public class PatrocinadorDTO {
    private Long id;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
}
