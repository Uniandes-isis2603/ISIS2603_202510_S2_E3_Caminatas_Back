package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.Date;

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
}
