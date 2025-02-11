package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.Date;
import java.util.List;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Data

public class PagoEntity {
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

    @OneToMany(mappedBy = "pago")
    private List<SeguroEntity> seguros;
}
