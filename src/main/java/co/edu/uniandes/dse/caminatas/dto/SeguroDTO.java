package co.edu.uniandes.dse.caminatas.dto;

import lombok.Data;

@Data
public class SeguroDTO {
    private Long id;
    private Long numero;
    private String nombre;
    private String tipo;
    private String cobertura;
    private String condiciones;
    private float costo;
    private CaminataDTO caminata;
}
