package com.marcoscoutozup.fatura.saldocartao;

import com.marcoscoutozup.fatura.fatura.Fatura;
import com.marcoscoutozup.fatura.transacao.Transacao;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class VerificarSaldo {

    private final EntityManager entityManager;

    public VerificarSaldo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public SaldoResponse calcularSaldoDisponivel(UUID idCartao, BigDecimal limite){
        Assert.notNull(idCartao, "O numero do cartão não pode ser nulo para calculo de saldo");
        Assert.notNull(limite, "O limite do cartão não pode ser nulo para calculo de saldo");

                    //1
        final List<Fatura> faturas = entityManager.createNamedQuery("findFaturaByCartaoAndData", Fatura.class)
                .setParameter("idCartao", idCartao)
                .setParameter("mes", LocalDate.now().getMonthValue())
                .setParameter("ano", LocalDate.now().getYear())
                .getResultList();

        //2
        if(faturas.isEmpty()){
            return new SaldoResponse(limite, new HashSet<>());
        }

        Fatura fatura = faturas.get(0);

        //3
        SaldoResponse saldoResponse =                                       //4
                new SaldoResponse(fatura.calcularSaldoDoCartao(limite), Transacao.toResponseSet(fatura.retornarAsUltimas10Transacoes()));

        return saldoResponse;
    }
}
