package com.fiap.pj.core.email.app.gateways;

import com.fiap.pj.core.email.domain.EmailMessage;

public interface EmailGateway {

    void dispatch(EmailMessage message);
}