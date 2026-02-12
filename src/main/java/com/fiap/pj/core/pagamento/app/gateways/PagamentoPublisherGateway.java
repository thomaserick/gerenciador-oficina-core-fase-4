package com.fiap.pj.core.pagamento.app.gateways;

import com.fiap.pj.core.pagamento.domain.event.PagamentoProcessadoEvent;

public interface PagamentoPublisherGateway {

    void processar(PagamentoProcessadoEvent event);

}
