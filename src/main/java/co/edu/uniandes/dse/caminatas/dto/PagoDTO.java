package co.edu.uniandes.dse.caminatas.dto;

import java.util.Date;
import lombok.Data;

@Data
public class PagoDTO {

	private Long id;
	private String numeroTransaccion;
	private float valorCaminata;
	private float valorTotal;
	private String numeroTarjeta;
	private Date fechaVencimiento;
	private String ccv;
    private int cuotas;
    private String propietario;

    
    private EmpresaDTO empresaDTO;
    private CaminanteDTO caminanteDTO;
    private CaminataDTO caminataDTO;
}

