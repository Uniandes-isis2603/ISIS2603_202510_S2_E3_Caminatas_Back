package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamFloatValue;
import uk.co.jemos.podam.common.PodamIntValue;
import uk.co.jemos.podam.common.PodamStringValue;

import java.util.Date;
import jakarta.persistence.ManyToOne;

@Entity
@Data

public class PagoEntity extends BaseEntity
{
    private String numeroTransaccion;

    @PodamFloatValue(minValue = 1)
    private float valorCaminata;

    @PodamFloatValue(minValue = 1)
    private float valorTotal;

    private Date fechaVencimiento;

    @PodamStringValue(length = 16)
    private String numeroTarjeta;

    @PodamStringValue(length = 3)
    private String ccv;

    @PodamIntValue(minValue = 0, maxValue = 10)
    private int cuotas;
    private String propietario;
    
    @PodamExclude
    @ManyToOne
    private EmpresaEntity empresa;

    @PodamExclude
    @ManyToOne
    private CaminanteEntity caminante;

    //@OneToMany(mappedBy = "pago", cascade = CascadeType.PERSIST, orphanRemoval = true)
    //private List<SeguroEntity> seguros = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private CaminataEntity caminata;
}
