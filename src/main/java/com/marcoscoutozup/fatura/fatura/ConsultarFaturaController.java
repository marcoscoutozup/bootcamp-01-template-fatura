package com.marcoscoutozup.fatura.fatura;

import com.marcoscoutozup.fatura.cartao.Cartao;
import com.marcoscoutozup.fatura.exceptions.StandardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes")
@Validated
public class ConsultarFaturaController {

    private final EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(ConsultarFaturaController.class);

    public ConsultarFaturaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/{idCartao}/faturas")
    public ResponseEntity buscarDetalhesDaFaturaCorrente(@PathVariable UUID idCartao){
        Integer mes = LocalDate.now().getMonthValue();
        Integer ano = LocalDate.now().getYear();
        return processarConsultarDeFatura(idCartao, mes, ano);
    }

    @GetMapping("/{idCartao}/faturas/{mes}/{ano}")
    public ResponseEntity buscarDetalhesDaFatura(@PathVariable UUID idCartao,
                                                 @PathVariable @Positive Integer mes,
                                                 @PathVariable @Positive Integer ano){
        return processarConsultarDeFatura(idCartao, mes, ano);
    }

    protected ResponseEntity processarConsultarDeFatura(UUID idCartao, Integer mes, Integer ano){
        log.info("[BUSCA DE DETALHES DA FATURA] Solicitação de detalhes da fatura do cartão: {}", idCartao);

                        //1
        final Optional<Cartao> cartaoProcurado = Optional.ofNullable(entityManager.find(Cartao.class, idCartao));

        //2
        if(cartaoProcurado.isEmpty()){
            log.warn("[BUSCA DE DETALHES DA FATURA] Cartão não foi encontrado. Número do cartão: {}", idCartao);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                //3
                    .body(new StandardException(HttpStatus.NOT_FOUND.value(), Arrays.asList("O cartão não foi encontrado")));
        }

                    //4
        final List<Fatura> fatura = entityManager.createNamedQuery("findFaturaByCartaoAndData", Fatura.class)
                .setParameter("idCartao", idCartao)
                .setParameter("mes", mes)
                .setParameter("ano", ano)
                .getResultList();

        //5
        if(fatura.isEmpty()) {
            log.warn("[BUSCA DE DETALHES DA FATURA] Não foram encontradas transacões para a fatura. Número do cartão: {}, Fatura: {}/{}", idCartao, mes, ano);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StandardException(HttpStatus.NOT_FOUND.value(), Arrays.asList("Não existem transações para a fatura do mês corrente")));
        }

        //6
        final FaturaResponse response = fatura.get(0).toResponse();
        return ResponseEntity.ok(response);
    }
}
