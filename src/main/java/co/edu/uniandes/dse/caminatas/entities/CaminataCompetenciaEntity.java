package co.edu.uniandes.dse.caminatas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class CaminataCompetenciaEntity extends CaminataEntity {
    private int numero;
    private String condicionesParticipacion;
    private String premios;
    private String requisitos;

    @ManyToOne
    private PatrocinadorEntity patrocinador;
}