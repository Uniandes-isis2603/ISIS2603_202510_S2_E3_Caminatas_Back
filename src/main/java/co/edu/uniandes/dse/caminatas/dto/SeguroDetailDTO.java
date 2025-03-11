package co.edu.uniandes.dse.caminatas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SeguroDetailDTO extends SeguroDTO{
    private List<CaminataDTO> caminatas = new ArrayList<>();
}
