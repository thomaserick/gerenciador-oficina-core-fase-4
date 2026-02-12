package com.fiap.pj.infra.pagamento.controller.openapi;

import com.fiap.pj.infra.pagamento.controller.request.ProcessarPagamentoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface PagamentoControllerOpenApi {

    @Operation(description = "Realizar Pagamento da Ordem de serviço", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Processa pagamento da ordem de serviço."),
            @ApiResponse(responseCode = "400", description = "O Processamento de pagamento da ordem de serviço não pode ser iniciado.")})
    ResponseEntity<Void> processarPagamento(@PathVariable UUID id, @RequestBody ProcessarPagamentoRequest request);
}
