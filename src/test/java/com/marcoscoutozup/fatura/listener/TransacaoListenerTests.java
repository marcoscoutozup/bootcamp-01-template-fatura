package com.marcoscoutozup.fatura.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransacaoListenerTests {

    private TransacaoListener listener;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    public void setup(){
        listener = new TransacaoListener(entityManager, null, null);
    }

    @Test
    @DisplayName("Deve lançar exceção se resposta de mensageria for nula")
    public void deveLancaoExcecaoSeRespostaDeMensageriaForNula(){
        assertThrows(IllegalArgumentException.class,
                () -> listener.ouvirTransacoesDaMensageria(null));
    }

}
