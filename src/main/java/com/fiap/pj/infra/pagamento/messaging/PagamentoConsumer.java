package com.fiap.pj.infra.pagamento.messaging;


import com.fiap.pj.core.ordemservico.app.usecase.AtualizarStatusPagamentoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverEntregueUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.command.AtualizarStatusPagamentoCommand;
import com.fiap.pj.core.ordemservico.domain.enums.PagamentoStatus;
import com.fiap.pj.core.pagamento.domain.event.PagamentoEvent;
import com.fiap.pj.infra.util.amqp.ContextAwareRabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PagamentoConsumer extends ContextAwareRabbitListener {

    private final MoverEntregueUseCase moverEntregueUseCase;
    private final AtualizarStatusPagamentoUseCase atualizarStatusPagamentoUseCase;

    public PagamentoConsumer(MoverEntregueUseCase moverEntregueUseCase, AtualizarStatusPagamentoUseCase atualizarStatusPagamentoUseCase) {
        this.moverEntregueUseCase = moverEntregueUseCase;
        this.atualizarStatusPagamentoUseCase = atualizarStatusPagamentoUseCase;
    }

    @Transactional
    @RabbitListener(queues = "${broker.queue.pagamento.autorizado}")
    public void receiveMessagePagamentoAutorizado(Message<PagamentoEvent> message) {
        executeWithContext(message, () -> moverEntregueUseCase.handle(message.getPayload().ordemServicoId()));
    }

    @Transactional
    @RabbitListener(queues = "${broker.queue.pagamento.naoautorizado}")
    public void receiveMessagePagamentoNaoAtorizado(Message<PagamentoEvent> message) {
        var cmd = new AtualizarStatusPagamentoCommand(message.getPayload().ordemServicoId(), PagamentoStatus.FALHOU);
        executeWithContext(message, () -> atualizarStatusPagamentoUseCase.handle(cmd));
    }
}
