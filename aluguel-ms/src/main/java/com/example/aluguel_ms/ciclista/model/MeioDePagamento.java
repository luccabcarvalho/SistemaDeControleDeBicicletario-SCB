package com.example.aluguel_ms.ciclista.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "meios_de_pagamento")
public class MeioDePagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nomeTitular;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDate validade;

    @Column(nullable = false)
    private String cvv;

    public MeioDePagamento() {
        // Construtor padrão exigido pelo JPA para a criação de instâncias.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public static MeioDePagamento fromMap(Map<String, Object> map) {
        MeioDePagamento m = new MeioDePagamento();
        m.nomeTitular = (String) map.get("nomeTitular");
        m.numero = (String) map.get("numero");
        m.validade = LocalDate.parse((String) map.get("validade"));
        m.cvv = (String) map.get("cvv");
        return m;
    }
}