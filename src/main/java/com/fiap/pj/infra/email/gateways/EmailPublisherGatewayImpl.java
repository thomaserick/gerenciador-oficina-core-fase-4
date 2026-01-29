package com.fiap.pj.infra.email.gateways;

import com.fiap.pj.core.email.app.gateways.EmailGateway;
import com.fiap.pj.core.email.domain.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

public class EmailPublisherGatewayImpl implements EmailGateway {

    final RabbitTemplate rabbitTemplate;


    @Value("${broker.queue.email}")
    private String routingKey;

    public EmailPublisherGatewayImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void dispatch(EmailMessage message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }
}