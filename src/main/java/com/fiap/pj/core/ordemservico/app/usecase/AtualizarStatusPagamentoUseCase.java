package com.fiap.pj.core.ordemservico.app.usecase;

import com.fiap.pj.core.ordemservico.app.usecase.command.AtualizarStatusPagamentoCommand;

public interface AtualizarStatusPagamentoUseCase {

    void handle(AtualizarStatusPagamentoCommand cmd);
}