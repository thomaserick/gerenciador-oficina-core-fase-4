package com.fiap.pj.core.pagamento.app;

import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.ordemservico.domain.OrdemServico;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoNaoEncontradaException;
import com.fiap.pj.core.pagamento.app.gateways.PagamentoPublisherGateway;
import com.fiap.pj.core.pagamento.domain.enums.MetodoPagamento;
import com.fiap.pj.core.pagamento.domain.event.PagamentoProcessadoEvent;
import com.fiap.pj.infra.pagamento.controller.request.ProcessarPagamentoRequest;
import com.fiap.pj.infra.util.security.SecurityContextUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ProcessarPagamentoUseCaseImplTest {

    private OrdemServicoGateway ordemServicoGateway;
    private PagamentoPublisherGateway pagamentoPublisherGateway;
    private ProcessarPagamentoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        ordemServicoGateway = mock(OrdemServicoGateway.class);
        pagamentoPublisherGateway = mock(PagamentoPublisherGateway.class);
        useCase = new ProcessarPagamentoUseCaseImpl(ordemServicoGateway, pagamentoPublisherGateway);
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        UUID ordemServicoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        BigDecimal valorTotal = new BigDecimal("100.00");
        MetodoPagamento metodoPagamento = MetodoPagamento.CARTAO_CREDITO;
        Integer qtdParcelas = 2;
        UUID usuarioId = UUID.randomUUID();

        // Dados do cartão
        String numeroCartao = "4111111111111111";
        String codigoSeguranca = "123";
        Integer mesExpiracao = 12;
        Integer anoExpiracao = 2028;
        String nomeTitular = "João da Silva";
        String cpfTitular = "12345678900";
        String emailTitular = "joao@email.com";

        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getUsuarioId).thenReturn(usuarioId);

            OrdemServico ordemServico = mock(OrdemServico.class);
            when(ordemServicoGateway.buscarPorId(ordemServicoId)).thenReturn(Optional.of(ordemServico));
            when(ordemServico.getId()).thenReturn(ordemServicoId);
            when(ordemServico.getClienteId()).thenReturn(clienteId);
            when(ordemServico.getValorTotal()).thenReturn(valorTotal);

            ProcessarPagamentoRequest request = new ProcessarPagamentoRequest(
                    ordemServicoId,
                    metodoPagamento,
                    qtdParcelas,
                    numeroCartao,
                    codigoSeguranca,
                    mesExpiracao,
                    anoExpiracao,
                    nomeTitular,
                    cpfTitular,
                    emailTitular
            );

            useCase.handle(request);

            verify(ordemServicoGateway).buscarPorId(ordemServicoId);
            verify(ordemServico).processarPagamento();
            verify(ordemServicoGateway).salvar(ordemServico);

            ArgumentCaptor<PagamentoProcessadoEvent> eventCaptor =
                    ArgumentCaptor.forClass(PagamentoProcessadoEvent.class);
            verify(pagamentoPublisherGateway).processar(eventCaptor.capture());

            PagamentoProcessadoEvent event = eventCaptor.getValue();
            assertEquals(ordemServicoId, event.ordemServicoId());
            assertEquals(clienteId, event.clienteId());
            assertEquals(valorTotal, event.valorTotal());
            assertEquals(BigDecimal.ZERO, event.desconto());
            assertEquals(valorTotal, event.valor());
            assertEquals(metodoPagamento, event.metodoPagamento());
            assertEquals(qtdParcelas, event.quantidadeParcelas());
            assertEquals(usuarioId, event.usuarioId());
            assertEquals(numeroCartao, event.numeroCartao());
            assertEquals(codigoSeguranca, event.codigoSeguranca());
            assertEquals(mesExpiracao, event.mesExpiracao());
            assertEquals(anoExpiracao, event.anoExpiracao());
            assertEquals(nomeTitular, event.nomeTitular());
            assertEquals(cpfTitular, event.cpfTitular());
            assertEquals(emailTitular, event.emailTitular());
        }
    }

    @Test
    void deveLancarExcecaoQuandoOrdemServicoNaoEncontrada() {
        UUID ordemServicoId = UUID.randomUUID();
        ProcessarPagamentoRequest request = new ProcessarPagamentoRequest(
                ordemServicoId,
                MetodoPagamento.CARTAO_DEBITO,
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(ordemServicoGateway.buscarPorId(ordemServicoId)).thenReturn(Optional.empty());

        assertThrows(OrdemServicoNaoEncontradaException.class, () -> useCase.handle(request));

        verify(ordemServicoGateway).buscarPorId(ordemServicoId);
        verifyNoMoreInteractions(ordemServicoGateway, pagamentoPublisherGateway);
    }
}
