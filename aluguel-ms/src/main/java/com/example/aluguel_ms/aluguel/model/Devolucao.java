
package com.example.aluguel_ms.aluguel.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
public class Devolucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    private Integer aluguelId;
    private Integer bicicletaId;
    private Integer trancaId;
    private LocalDateTime dataHoraDevolucao;
    private LocalDateTime dataHoraCobranca;
    private Double valorExtra;
    private String cartao;
    private String statusPagamento;
    private String statusBicicleta;
    private String statusTranca;
    private String emailCiclista;
    private boolean reparoSolicitado;
    private boolean pagamentoAutorizado;

    // Getters e setters
    public Integer getAluguelId() { return aluguelId; }
    public void setAluguelId(Integer aluguelId) { this.aluguelId = aluguelId; }
    public Integer getBicicletaId() { return bicicletaId; }
    public void setBicicletaId(Integer bicicletaId) { this.bicicletaId = bicicletaId; }
    public Integer getTrancaId() { return trancaId; }
    public void setTrancaId(Integer trancaId) { this.trancaId = trancaId; }
    public LocalDateTime getDataHoraDevolucao() { return dataHoraDevolucao; }
    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) { this.dataHoraDevolucao = dataHoraDevolucao; }
    public LocalDateTime getDataHoraCobranca() { return dataHoraCobranca; }
    public void setDataHoraCobranca(LocalDateTime dataHoraCobranca) { this.dataHoraCobranca = dataHoraCobranca; }
    public Double getValorExtra() { return valorExtra; }
    public void setValorExtra(Double valorExtra) { this.valorExtra = valorExtra; }
    public String getCartao() { return cartao; }
    public void setCartao(String cartao) { this.cartao = cartao; }
    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }
    public String getStatusBicicleta() { return statusBicicleta; }
    public void setStatusBicicleta(String statusBicicleta) { this.statusBicicleta = statusBicicleta; }
    public String getStatusTranca() { return statusTranca; }
    public void setStatusTranca(String statusTranca) { this.statusTranca = statusTranca; }
    public String getEmailCiclista() { return emailCiclista; }
    public void setEmailCiclista(String emailCiclista) { this.emailCiclista = emailCiclista; }
    public boolean isReparoSolicitado() { return reparoSolicitado; }
    public void setReparoSolicitado(boolean reparoSolicitado) { this.reparoSolicitado = reparoSolicitado; }
    public boolean isPagamentoAutorizado() { return pagamentoAutorizado; }
    public void setPagamentoAutorizado(boolean pagamentoAutorizado) { this.pagamentoAutorizado = pagamentoAutorizado; }
    public Integer getIdBicicleta() { return getBicicletaId(); }
    public void setIdBicicleta(Integer id) { setBicicletaId(id); }
    public Integer getIdTranca() { return getTrancaId(); }
    public void setIdTranca(Integer id) { setTrancaId(id); }
}
