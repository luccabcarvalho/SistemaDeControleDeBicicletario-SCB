package com.example.aluguel_ms.aluguel.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alugueis")
public class Aluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer ciclista;

    @Column(nullable = false)
    private Integer bicicleta;

    @Column(nullable = false)
    private Integer trancaInicio;

    @Column
    private Integer trancaFim;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column
    private LocalDateTime horaFim;

    @Column(nullable = false)
    private Integer cobranca;

    public Aluguel() {
        // JPA exige um construtor público sem argumentos para instanciar entidades
    }

    // Getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCiclista() { return ciclista; }
    public void setCiclista(Integer ciclista) { this.ciclista = ciclista; }
    public Integer getBicicleta() { return bicicleta; }
    public void setBicicleta(Integer bicicleta) { this.bicicleta = bicicleta; }
    public Integer getTrancaInicio() { return trancaInicio; }
    public void setTrancaInicio(Integer trancaInicio) { this.trancaInicio = trancaInicio; }
    public Integer getTrancaFim() { return trancaFim; }
    public void setTrancaFim(Integer trancaFim) { this.trancaFim = trancaFim; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalDateTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalDateTime horaFim) { this.horaFim = horaFim; }
    public Integer getCobranca() { return cobranca; }
    public void setCobranca(Integer cobranca) { this.cobranca = cobranca; }
}
