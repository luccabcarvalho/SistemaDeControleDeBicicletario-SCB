package com.example.aluguel_ms.ciclista.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Embeddable
public class Passaporte {
    private String numero;
    private LocalDate validade;
    private String pais;

    public Passaporte() {}

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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public static Passaporte fromMap(Map<String, Object> map) {
        Passaporte p = new Passaporte();
        p.numero = (String) map.get("numero");
        p.validade = LocalDate.parse((String) map.get("validade"));
        p.pais = (String) map.get("pais");
        return p;
    }
}