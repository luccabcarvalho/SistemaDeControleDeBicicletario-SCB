package com.example.aluguel_ms.funcionario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String funcao;

    @Column(nullable = false)
    private int idade;

    public Funcionario() {}

    public Funcionario(String matricula, String nome, String email, String senha, String cpf, String funcao, int idade) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.funcao = funcao;
        this.idade = idade;
    }

    public String getMatricula() { return matricula; }

    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }

    public void setSenha(String senha) { this.senha = senha; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getFuncao() { return funcao; }

    public void setFuncao(String funcao) { this.funcao = funcao; }

    public int getIdade() { return idade; }

    public void setIdade(int idade) { this.idade = idade; }
}