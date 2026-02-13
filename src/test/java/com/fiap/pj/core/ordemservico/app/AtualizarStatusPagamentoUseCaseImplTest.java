package com.fiap.pj.core.ordemservico.app;

import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.ordemservico.app.usecase.command.AtualizarStatusPagamentoCommand;
import com.fiap.pj.core.ordemservico.domain.OrdemServico;
import com.fiap.pj.core.ordemservico.domain.enums.PagamentoStatus;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoNaoEncontradaException;
import com.fiap.pj.util.TestSecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusPagamentoUseCaseImplTest {

    @Mock
    private OrdemServicoGateway ordemServicoGateway;

    @InjectMocks
    private AtualizarStatusPagamentoUseCaseImpl atualizarStatusPagamentoUseCaseImpl;

    @Captor
    private ArgumentCaptor<OrdemServico> ordemServicoCaptor;

    @Test
    void deveAtualizarStatusPagamento() {
        TestSecurityConfig.setAuthentication();

        UUID ordemServicoId = UUID.randomUUID();
        PagamentoStatus novoStatus = PagamentoStatus.PAGO;

        OrdemServico ordemServico = mock(OrdemServico.class);

        when(ordemServicoGateway.buscarPorId(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        var cmd = new AtualizarStatusPagamentoCommand(ordemServicoId, novoStatus);

        atualizarStatusPagamentoUseCaseImpl.handle(cmd);

        verify(ordemServicoGateway).buscarPorId(ordemServicoId);
        verify(ordemServico).atualizarStatusPagamento(novoStatus);
        verify(ordemServicoGateway).salvar(ordemServico);
    }

    @Test
    void deveLancarExcecaoQuandoOrdemServicoNaoEncontrada() {
        TestSecurityConfig.setAuthentication();

        UUID ordemServicoId = UUID.randomUUID();
        PagamentoStatus novoStatus = PagamentoStatus.PAGO;

        when(ordemServicoGateway.buscarPorId(ordemServicoId))
                .thenReturn(Optional.empty());

        var cmd = new AtualizarStatusPagamentoCommand(ordemServicoId, novoStatus);

        Assertions.assertThrows(
                OrdemServicoNaoEncontradaException.class,
                () -> atualizarStatusPagamentoUseCaseImpl.handle(cmd)
        );

        verify(ordemServicoGateway).buscarPorId(ordemServicoId);
        verify(ordemServicoGateway, never()).salvar(any());
    }
}
