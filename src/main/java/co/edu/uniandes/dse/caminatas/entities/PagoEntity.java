package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;
import jakarta.persistence.ManyToOne;

@Entity
@Data

public class PagoEntity extends BaseEntity{
    private int numeroTransaccion;
    private float costoTotal;
    private Date fecha;
    private String medioPago;
    private int numeroTarjeta;
    private Date fechaVencimiento;
    private int codigoSeguridad;
    private int numeroCuotas;
    private int codigoPostal;
    private String direccion;
    
    @ManyToOne
    private EmpresaEntity empresa;

    @ManyToOne
    private CaminanteEntity caminante;

    @ManyToOne
    private CaminataEntity caminata;
}
