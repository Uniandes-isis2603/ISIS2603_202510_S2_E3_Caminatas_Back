package co.edu.uniandes.dse.caminatas.dto;

import lombok.Data;

@Data
public class BlogDTO {
    private Long id;
    private String titulo;
    private String foto;
    private String video;
    private String text;
    private CaminanteDTO caminanteDTO;
}