package com.fiap.pj.core.pagamento.domain.event;


import com.fiap.pj.core.pagamento.domain.enums.MetodoPagamento;

import java.math.BigDecimal;
import java.util.UUID;

public record PagamentoProcessadoEvent(UUID ordemServicoId,
                                       UUID clienteId,
                                       BigDecimal valor,
                                       BigDecimal desconto,
                                       BigDecimal valorTotal,
                                       MetodoPagamento metodoPagamento,
                                       Integer quantidadeParcelas,
                                       UUID usuarioId) {
}
