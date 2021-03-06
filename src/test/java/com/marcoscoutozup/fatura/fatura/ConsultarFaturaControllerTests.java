package com.marcoscoutozup.fatura.fatura;

import com.marcoscoutozup.fatura.cartao.Cartao;
import com.marcoscoutozup.fatura.exceptions.StandardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.*;

public class ConsultarFaturaControllerTests {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery queryCartao;

    @Mock
    private TypedQuery queryFatura;

    @Mock
    private Fatura fatura;

    private ConsultarFaturaController controller;

    @BeforeEach
    public void setup(){
        initMocks(this);
        controller = new ConsultarFaturaController(entityManager);
    }

    @Test
    @DisplayName("Deve retornar NotFound quanto cartão não for encontrado")
    public void deveRetornarNotFoundQuandoCartaoNaoForEncontrado(){
        when(entityManager.createNamedQuery(any(), any(Class.class))).thenReturn(queryCartao);
        when(queryCartao.setParameter(anyString(), any())).thenReturn(queryCartao);
        when(queryCartao.getResultList()).thenReturn(new ArrayList());
        ResponseEntity responseEntity = controller.processarConsultarDeFatura(UUID.randomUUID(), null, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof StandardException);
    }

    @Test
    @DisplayName("Deve retornar NotFound quanto não houver transações para o cartão")
    public void deveRetornarNotFoundQuandoNaoHouverTransacaoParaOCartao(){
        when(entityManager.createNamedQuery(anyString(), eq(Cartao.class))).thenReturn(queryCartao);
        when(queryCartao.setParameter(anyString(), any())).thenReturn(queryCartao);
        when(queryCartao.getResultList()).thenReturn(List.of(new Cartao()));

        when(entityManager.createNamedQuery(anyString(), eq(Fatura.class))).thenReturn(queryFatura);
        when(queryFatura.setParameter(anyString(), any())).thenReturn(queryFatura);
        when(queryFatura.getResultList()).thenReturn(new ArrayList());

        ResponseEntity responseEntity = controller.buscarDetalhesDaFatura(UUID.randomUUID(), new Random().nextInt(), new Random().nextInt());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof StandardException);
    }

    @Test
    @DisplayName("Deve retornar detalhes da fatura")
    public void deveRetornarDetalhesDaFatura(){
        when(entityManager.createNamedQuery(anyString(), eq(Cartao.class))).thenReturn(queryCartao);
        when(queryCartao.setParameter(anyString(), any())).thenReturn(queryCartao);
        when(queryCartao.getResultList()).thenReturn(List.of(new Cartao()));

        when(entityManager.createNamedQuery(anyString(), eq(Fatura.class))).thenReturn(queryFatura);
        when(queryFatura.setParameter(anyString(), any())).thenReturn(queryFatura);
        when(queryFatura.getResultList()).thenReturn(List.of(new Fatura()));

        ResponseEntity responseEntity = controller.buscarDetalhesDaFatura(UUID.randomUUID(), new Random().nextInt(), new Random().nextInt());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof StandardException);
    }

}
