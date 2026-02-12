package com.fiap.pj.infra.config;


import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.pagamento.app.ProcessarPagamentoUseCaseImpl;
import com.fiap.pj.core.pagamento.app.gateways.PagamentoPublisherGateway;
import com.fiap.pj.core.pagamento.app.usecase.ProcessarPagamentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfig {

    @Bean
    ProcessarPagamentoUseCase processarPagamentoUseCase(OrdemServicoGateway ordemServicoGateway, PagamentoPublisherGateway pagamentoPublisherGateway) {
        return new ProcessarPagamentoUseCaseImpl(ordemServicoGateway, pagamentoPublisherGateway);
    }

}
