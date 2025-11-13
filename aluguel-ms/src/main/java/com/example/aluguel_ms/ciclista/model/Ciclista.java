package com.example.aluguel_ms.ciclista.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "ciclistas")
public class Ciclista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate nascimento;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Embedded
    private Passaporte passaporte;

    @Column(nullable = false)
    private String nacionalidade;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String urlFotoDocumento;

    @Column(nullable = false)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meio_de_pagamento_id")
    private MeioDePagamento meioDePagamento;

    public Ciclista() {
        // Construtor padrão exigido pelo JPA para a criação de instâncias.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Passaporte getPassaporte() {
        return passaporte;
    }

    public void setPassaporte(Passaporte passaporte) {
        this.passaporte = passaporte;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlFotoDocumento() {
        return urlFotoDocumento;
    }

    public void setUrlFotoDocumento(String urlFotoDocumento) {
        this.urlFotoDocumento = urlFotoDocumento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public MeioDePagamento getMeioDePagamento() {
        return meioDePagamento;
    }

    public void setMeioDePagamento(MeioDePagamento meioDePagamento) {
        this.meioDePagamento = meioDePagamento;
    }

    public static Ciclista fromMap(Map<String, Object> map) {
        Ciclista c = new Ciclista();
        c.nome = (String) map.get("nome");
        c.nascimento = LocalDate.parse((String) map.get("nascimento"));
        c.cpf = (String) map.get("cpf");
        Map<String, Object> passaporteMap = (Map<String, Object>) map.get("passaporte");
        if (passaporteMap != null) {
            c.passaporte = Passaporte.fromMap(passaporteMap);
        }
        c.nacionalidade = (String) map.get("nacionalidade");
        c.email = (String) map.get("email");
        c.urlFotoDocumento = (String) map.get("urlFotoDocumento");
        c.senha = (String) map.get("senha");
        return c;
    }
}