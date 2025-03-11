package co.edu.uniandes.dse.caminatas.dto;

import lombok.Data;

@Data
public class CaminataCompetenciaDTO {
    private Long id;
    private int numero;
    private String condicionesParticipacion;
    private String premios;
    private String requisitos;
    private CaminataCompetenciaDTO camiataCompetenciaDTO;
}
