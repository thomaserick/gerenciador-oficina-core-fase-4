package com.fiap.pj.infra.pagamento.messaging;


import com.fiap.pj.core.ordemservico.app.usecase.MoverEntregueUseCase;
import com.fiap.pj.core.pagamento.domain.event.PagamentoRealizadoEvent;
import com.fiap.pj.infra.util.amqp.ContextAwareRabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PagamentoConsumer extends ContextAwareRabbitListener {

    private final MoverEntregueUseCase moverEntregueUseCase;

    public PagamentoConsumer(MoverEntregueUseCase moverEntregueUseCase) {
        this.moverEntregueUseCase = moverEntregueUseCase;
    }

    @RabbitListener(queues = "${broker.queue.pagamento.autorizado}")
    @Transactional
    public void receiveMessage(Message<PagamentoRealizadoEvent> message) {
        executeWithContext(message, () -> moverEntregueUseCase.handle(message.getPayload().ordemServicoId()));
    }
}
