package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data

public class CaminataDetailDTO extends CaminataDTO {
    private EmpresaDTO empresa;
    private SeguroDTO seguro;
    private List<CaminanteDTO> caminantes = new ArrayList<>();
    private List<PagoDTO> pagos = new ArrayList<>();
    private SeguroDTO seguro;
}
