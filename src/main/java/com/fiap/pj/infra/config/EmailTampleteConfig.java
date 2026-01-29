package com.fiap.pj.infra.config;

import com.fiap.pj.core.email.app.EnviarEmailUseCaseImpl;
import com.fiap.pj.core.email.app.gateways.EmailGateway;
import com.fiap.pj.infra.email.gateways.EmailPublisherGatewayImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailTampleteConfig {

    @Bean
    EnviarEmailUseCaseImpl enviarEmailUseCase(EmailGateway emailGateway) {
        return new EnviarEmailUseCaseImpl(emailGateway);
    }

    @Bean
    EmailPublisherGatewayImpl emailTemplateGateway(RabbitTemplate rabbitTemplate) {
        return new EmailPublisherGatewayImpl(rabbitTemplate);
    }
}