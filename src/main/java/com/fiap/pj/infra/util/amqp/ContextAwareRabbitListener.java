package com.fiap.pj.infra.util.amqp;

import com.fiap.pj.infra.util.security.SecurityContextUtils;
import org.springframework.messaging.Message;


public abstract class ContextAwareRabbitListener {

    protected void executeWithContext(Message message, Runnable action) {
        try {
            SecurityContextUtils.configurarSecurityContext(message);
            action.run();
        } finally {
            SecurityContextUtils.clear();
        }
    }

}
