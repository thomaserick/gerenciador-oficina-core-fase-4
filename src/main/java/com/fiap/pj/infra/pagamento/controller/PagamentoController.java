package com.fiap.pj.infra.pagamento.controller;

import com.fiap.pj.core.pagamento.app.usecase.ProcessarPagamentoUseCase;
import com.fiap.pj.infra.pagamento.controller.openapi.PagamentoControllerOpenApi;
import com.fiap.pj.infra.pagamento.controller.request.ProcessarPagamentoRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = PagamentoController.PATH)
@AllArgsConstructor
public class PagamentoController implements PagamentoControllerOpenApi {

    public static final String PATH = "v1/ordens-servicos/{id}/pagamentos";

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    @Override
    @PostMapping
    public ResponseEntity<Void> processarPagamento(UUID id, ProcessarPagamentoRequest request) {
        processarPagamentoUseCase.handle(request.comOrdemServicoId(id));
        return ResponseEntity.accepted().build();
    }
}