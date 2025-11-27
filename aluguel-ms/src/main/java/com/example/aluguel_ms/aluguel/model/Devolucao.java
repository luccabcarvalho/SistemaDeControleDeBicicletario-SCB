package com.example.aluguel_ms.aluguel.model;

import java.time.LocalDateTime;

public class Devolucao {
    private Integer idBicicleta;
    private Integer idTranca;
    private LocalDateTime dataHoraDevolucao;
    private LocalDateTime dataHoraCobranca;
    private Double valorExtra;
    private String cartaoCobranca;
    private String statusBicicleta;
    private String statusTranca;
    private String emailCiclista;
    private boolean reparoSolicitado;
    private boolean pagamentoAutorizado;

    // Getters e setters
    public Integer getIdBicicleta() { return idBicicleta; }
    public void setIdBicicleta(Integer idBicicleta) { this.idBicicleta = idBicicleta; }
    public Integer getIdTranca() { return idTranca; }
    public void setIdTranca(Integer idTranca) { this.idTranca = idTranca; }
    public LocalDateTime getDataHoraDevolucao() { return dataHoraDevolucao; }
    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) { this.dataHoraDevolucao = dataHoraDevolucao; }
    public LocalDateTime getDataHoraCobranca() { return dataHoraCobranca; }
    public void setDataHoraCobranca(LocalDateTime dataHoraCobranca) { this.dataHoraCobranca = dataHoraCobranca; }
    public Double getValorExtra() { return valorExtra; }
    public void setValorExtra(Double valorExtra) { this.valorExtra = valorExtra; }
    public String getCartaoCobranca() { return cartaoCobranca; }
    public void setCartaoCobranca(String cartaoCobranca) { this.cartaoCobranca = cartaoCobranca; }
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
}
