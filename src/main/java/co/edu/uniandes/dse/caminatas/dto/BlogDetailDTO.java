package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BlogDetailDTO extends BlogDTO {
    private List<CaminanteDTO> caminantes = new ArrayList<>();
    
}
