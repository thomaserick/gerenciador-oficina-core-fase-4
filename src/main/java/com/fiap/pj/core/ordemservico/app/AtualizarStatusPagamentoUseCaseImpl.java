package com.fiap.pj.core.ordemservico.app;

import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.ordemservico.app.usecase.AtualizarStatusPagamentoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.command.AtualizarStatusPagamentoCommand;
import com.fiap.pj.core.ordemservico.domain.OrdemServico;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoNaoEncontradaException;


public class AtualizarStatusPagamentoUseCaseImpl implements AtualizarStatusPagamentoUseCase {

    private final OrdemServicoGateway ordemServicoGateway;

    public AtualizarStatusPagamentoUseCaseImpl(OrdemServicoGateway ordemServicoGateway) {
        this.ordemServicoGateway = ordemServicoGateway;
    }

    @Override
    public void handle(AtualizarStatusPagamentoCommand cmd) {
        OrdemServico ordemServico = this.ordemServicoGateway.buscarPorId(cmd.ordemServicoId()).orElseThrow(OrdemServicoNaoEncontradaException::new);
        ordemServico.atualizarStatusPagamento(cmd.pagamentoStatus());
        this.ordemServicoGateway.salvar(ordemServico);
    }


}