package com.fiap.pj.infra.pagamento.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.pj.core.pagamento.domain.enums.MetodoPagamento;

import java.util.UUID;

public record ProcessarPagamentoRequest(@JsonIgnore
                                        UUID ordemServicoId,
                                        MetodoPagamento metodoPagamento,
                                        Integer quantidadeParcelas
) {

    public ProcessarPagamentoRequest comOrdemServicoId(UUID ordemServicoId) {
        return new ProcessarPagamentoRequest(ordemServicoId, this.metodoPagamento, this.quantidadeParcelas);
    }

}
