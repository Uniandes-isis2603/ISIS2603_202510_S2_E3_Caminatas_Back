package co.edu.uniandes.dse.caminatas.dto;


import java.time.LocalTime;
import java.util.Date;

import lombok.Data;

@Data
public class CaminataCompetenciaDTO {
    private Long id;
    private int numero;
    private String condicionesParticipacion;
    private String premios;
    private String requisitos;
    private String titulo;
    private String tipo;
    private Date fecha;
    private LocalTime hora;
    private String departamento;
    private String ciudad;
    private float duracionEstimadaMinutos;
}
