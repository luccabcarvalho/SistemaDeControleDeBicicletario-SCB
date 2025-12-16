
package com.example.aluguel_ms.ciclista.model;

import jakarta.persistence.*;
import java.time.YearMonth;
import java.util.Map;

@Entity
@Table(name = "meios_de_pagamento")

public class MeioDePagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String nomeTitular;

    // Agora espera apenas ano-mês (yyyy-MM)
    @Column(nullable = false)
    private YearMonth validade;

    @Column(nullable = false)
    private String cvv;

    public MeioDePagamento() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public YearMonth getValidade() {
        return validade;
    }

    public void setValidade(YearMonth validade) {
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
        m.numero = (String) map.get("numero");
        m.nomeTitular = (String) map.get("nomeTitular");
        // Espera string no formato yyyy-MM
        Object validadeObj = map.get("validade");
        if (validadeObj != null) {
            m.validade = YearMonth.parse(validadeObj.toString());
        }
        m.cvv = (String) map.get("cvv");
        return m;
    }
}