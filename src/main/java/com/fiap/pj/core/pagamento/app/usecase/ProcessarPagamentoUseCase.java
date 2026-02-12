package com.fiap.pj.core.pagamento.app.usecase;

import com.fiap.pj.infra.pagamento.controller.request.ProcessarPagamentoRequest;

public interface ProcessarPagamentoUseCase {

    void handle(ProcessarPagamentoRequest request);
}