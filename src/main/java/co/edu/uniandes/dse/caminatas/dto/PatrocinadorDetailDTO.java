package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PatrocinadorDetailDTO extends PatrocinadorDTO {
    private List<CaminataCompetenciaDTO> caminatasCompetencia = new ArrayList<>();
}
