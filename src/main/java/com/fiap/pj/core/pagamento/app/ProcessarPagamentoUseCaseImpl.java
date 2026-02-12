package com.fiap.pj.core.pagamento.app;

import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.ordemservico.domain.OrdemServico;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoNaoEncontradaException;
import com.fiap.pj.core.pagamento.app.gateways.PagamentoPublisherGateway;
import com.fiap.pj.core.pagamento.app.usecase.ProcessarPagamentoUseCase;
import com.fiap.pj.core.pagamento.domain.event.PagamentoProcessadoEvent;
import com.fiap.pj.infra.pagamento.controller.request.ProcessarPagamentoRequest;
import com.fiap.pj.infra.util.security.SecurityContextUtils;

import java.math.BigDecimal;

public class ProcessarPagamentoUseCaseImpl implements ProcessarPagamentoUseCase {

    private final OrdemServicoGateway ordemServicoGateway;
    private final PagamentoPublisherGateway pagamentoPublisherGateway;

    public ProcessarPagamentoUseCaseImpl(OrdemServicoGateway ordemServicoGateway, PagamentoPublisherGateway pagamentoPublisherGateway) {
        this.ordemServicoGateway = ordemServicoGateway;
        this.pagamentoPublisherGateway = pagamentoPublisherGateway;
    }


    @Override
    public void handle(ProcessarPagamentoRequest request) {
        OrdemServico ordemServico = this.ordemServicoGateway.buscarPorId(request.ordemServicoId()).orElseThrow(OrdemServicoNaoEncontradaException::new);
        ordemServico.processarPagamento();
        this.ordemServicoGateway.salvar(ordemServico);

        var event = new PagamentoProcessadoEvent(ordemServico.getId(), ordemServico.getClienteId(), ordemServico.getValorTotal(),
                BigDecimal.ZERO, ordemServico.getValorTotal(), request.metodoPagamento(), request.quantidadeParcelas(),
                SecurityContextUtils.getUsuarioId());

        pagamentoPublisherGateway.processar(event);

    }
}