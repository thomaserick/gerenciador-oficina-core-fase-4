package com.fiap.pj.core.email.app;

import com.fiap.pj.core.email.app.gateways.EmailGateway;
import com.fiap.pj.core.email.app.usecase.EnviarEmailUseCase;
import com.fiap.pj.core.email.app.usecase.command.EnviarEmailCommand;
import com.fiap.pj.core.email.domain.EmailMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnviarEmailUseCaseImpl implements EnviarEmailUseCase {

    final EmailGateway emailGateway;

    public EnviarEmailUseCaseImpl(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public void handle(EnviarEmailCommand cmd) {
        var event = new EmailMessage(cmd.destinatario(), cmd.template(), cmd.placeholders());
        emailGateway.dispatch(event);
    }

}