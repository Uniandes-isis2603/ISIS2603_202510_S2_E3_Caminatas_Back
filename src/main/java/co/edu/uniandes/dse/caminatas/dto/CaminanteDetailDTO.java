package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CaminanteDetailDTO extends CaminanteDTO {
    private List<BlogDTO> blogsInteractuados = new ArrayList<>();
    private List<BlogDTO> blogsCreados = new ArrayList<>();
    private List<CaminataDTO> caminatas = new ArrayList<>();
    private List<PagoDTO> pagos = new ArrayList<>();
}
