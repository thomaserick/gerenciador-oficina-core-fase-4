package com.fiap.pj.infra.config;


import com.fiap.pj.core.cliente.app.gateways.ClienteGateway;
import com.fiap.pj.core.email.app.usecase.EnviarEmailUseCase;
import com.fiap.pj.core.observability.app.gateways.ObservabilityGateway;
import com.fiap.pj.core.ordemservico.app.AtualizarStatusPagamentoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.BuscarAcompanhamentoByOrdemServicoIdUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.CriarOrdemServicoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.ListarOrdemServicoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverAguardandoAprovacaoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverAguardandoRetiradaUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverEmDiagnosticoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverEmExecucaoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverEntregueUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.MoverFinalizadaUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.RealizarDiagnosticoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.RegistrarStatusOrdemServicoUseCaseImpl;
import com.fiap.pj.core.ordemservico.app.gateways.OrdemServicoGateway;
import com.fiap.pj.core.ordemservico.app.usecase.AlterarStatusOsAguardandoAprovacaoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.BuscarAcompanhamentoByOrdemServicoIdUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.CriarOrdemServicoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.ListarOrdemServicoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverAguardandoRetiradaUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverEmDiagnosticoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverEmExecucaoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverEntregueUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.MoverFinalizadaUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.RealizarDiagnosticoUseCase;
import com.fiap.pj.core.ordemservico.app.usecase.RegistrarStatusOrdemServicoUseCase;
import com.fiap.pj.infra.ordemservico.gateways.OrdemServicoRepositoryGatewayImpl;
import com.fiap.pj.infra.ordemservico.persistence.OrdemServicoRepositoryJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdemServicoConfig {

    @Bean
    CriarOrdemServicoUseCase criarOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway, ObservabilityGateway observabilityGateway
    ) {
        return new CriarOrdemServicoUseCaseImpl(ordemServicoGateway, observabilityGateway);
    }

    @Bean
    AlterarStatusOsAguardandoAprovacaoUseCase alterarStatusOsAguardandoAprovacaoUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway) {
        return new MoverAguardandoAprovacaoUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway);
    }


    @Bean
    MoverAguardandoRetiradaUseCase moverAguardandoRetiradaUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway) {
        return new MoverAguardandoRetiradaUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway);
    }

    @Bean
    MoverEmDiagnosticoUseCase moverEmDiagnosticoUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway, RegistrarStatusOrdemServicoUseCase registrarStatusOrdemServicoUseCase) {
        return new MoverEmDiagnosticoUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway, registrarStatusOrdemServicoUseCase);
    }

    @Bean
    MoverEmExecucaoUseCase moverEmExecucaoUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway, RegistrarStatusOrdemServicoUseCase registrarStatusOrdemServicoUseCase) {
        return new MoverEmExecucaoUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway, registrarStatusOrdemServicoUseCase);
    }

    @Bean
    MoverEntregueUseCase moverEntregueUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway) {
        return new MoverEntregueUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway);
    }

    @Bean
    MoverFinalizadaUseCase moverFinalizadaUseCase(OrdemServicoGateway ordemServicoGateway, EnviarEmailUseCase enviarEmailUseCase, ClienteGateway clienteGateway, RegistrarStatusOrdemServicoUseCase registrarStatusOrdemServicoUseCase) {
        return new MoverFinalizadaUseCaseImpl(ordemServicoGateway, enviarEmailUseCase, clienteGateway, registrarStatusOrdemServicoUseCase);
    }

    @Bean
    RealizarDiagnosticoUseCase realizarDiagnosticoUseCase(OrdemServicoGateway ordemServicoGateway) {
        return new RealizarDiagnosticoUseCaseImpl(ordemServicoGateway);
    }

    @Bean
    ListarOrdemServicoUseCase listarOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway) {
        return new ListarOrdemServicoUseCaseImpl(ordemServicoGateway);
    }

    @Bean
    BuscarAcompanhamentoByOrdemServicoIdUseCase buscarAcompanhamentoByOrdemServicoIdUseCase(OrdemServicoGateway ordemServicoGateway) {
        return new BuscarAcompanhamentoByOrdemServicoIdUseCaseImpl(ordemServicoGateway);
    }

    @Bean
    RegistrarStatusOrdemServicoUseCase registrarStatusOrdemServicoUseCase(ObservabilityGateway observabilityGateway) {
        return new RegistrarStatusOrdemServicoUseCaseImpl(observabilityGateway);
    }

    @Bean
    AtualizarStatusPagamentoUseCaseImpl atualizarStatusPagamentoUseCase(OrdemServicoGateway ordemServicoGateway) {
        return new AtualizarStatusPagamentoUseCaseImpl(ordemServicoGateway);
    }

    @Bean
    OrdemServicoGateway ordemServicoGateway(OrdemServicoRepositoryJpa ordemServicoRepositoryJpa) {
        return new OrdemServicoRepositoryGatewayImpl(ordemServicoRepositoryJpa);
    }

}
