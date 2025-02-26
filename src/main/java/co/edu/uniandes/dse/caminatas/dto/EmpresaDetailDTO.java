package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EmpresaDetailDTO extends EmpresaDTO {
    private List<CaminataDTO> caminatas = new ArrayList<>();
    private List<PagoDTO> pagos = new ArrayList<>();
}
