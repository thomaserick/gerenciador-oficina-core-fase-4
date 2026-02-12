package com.fiap.pj.infra.pagamento.gateways;

import com.fiap.pj.core.pagamento.app.gateways.PagamentoPublisherGateway;
import com.fiap.pj.core.pagamento.domain.event.PagamentoProcessadoEvent;
import com.fiap.pj.infra.util.security.SecurityContextUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PagamentoPublisherGatewayImpl implements PagamentoPublisherGateway {

    final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.pagamento.processar}")
    private String routingKey;

    public PagamentoPublisherGatewayImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void processar(PagamentoProcessadoEvent event) {
        rabbitTemplate.convertAndSend(routingKey, event, message -> {
            message.getMessageProperties().setHeader("userId", SecurityContextUtils.getUsuarioId());
            return message;
        });
    }
}
