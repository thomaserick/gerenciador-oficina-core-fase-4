package com.fiap.pj.core.ordemservico.app.usecase.command;

import com.fiap.pj.core.ordemservico.domain.enums.PagamentoStatus;

import java.util.UUID;

public record AtualizarStatusPagamentoCommand(UUID ordemServicoId, PagamentoStatus pagamentoStatus) {
}
