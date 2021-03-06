package com.marcoscoutozup.fatura.parcelarfatura.client;

import com.marcoscoutozup.fatura.parcelarfatura.ParcelamentoDeFatura;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

public class ParcelamentoDaFaturaRequestClient {

    private UUID identificadorDaFatura;
    private Integer quantidade;
    private BigDecimal valor;

    public ParcelamentoDaFaturaRequestClient(ParcelamentoDeFatura parcelamentoDeFatura) {
        Assert.notNull(parcelamentoDeFatura, "A requisição de parcelamento de fatura não pode ser construída a partir de um parcelamento de fatura nulo");
        this.identificadorDaFatura = parcelamentoDeFatura.getFatura().getId();
        this.quantidade = parcelamentoDeFatura.getParcelas();
        this.valor = parcelamentoDeFatura.getValorDaParcela();
    }

    public UUID getIdentificadorDaFatura() {
        return identificadorDaFatura;
    }

    public void setIdentificadorDaFatura(UUID identificadorDaFatura) {
        this.identificadorDaFatura = identificadorDaFatura;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
