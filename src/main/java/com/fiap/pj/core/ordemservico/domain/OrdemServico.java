package com.fiap.pj.core.ordemservico.domain;

import com.fiap.pj.core.orcamento.domain.Orcamento;
import com.fiap.pj.core.ordemservico.domain.enums.OrdemServicoStatus;
import com.fiap.pj.core.ordemservico.domain.enums.PagamentoStatus;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoAguardandoAprovacaoException;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoAguardandoRetiradaException;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoDiagnosticoException;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoEmExecucaoException;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoEntregueException;
import com.fiap.pj.core.ordemservico.exception.OrdemServicoExceptions.OrdemServicoStatusInvalidoFinalizadaException;
import com.fiap.pj.core.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.fiap.pj.core.ordemservico.domain.enums.OrdemServicoStatus.AGUARDANDO_APROVACAO;
import static com.fiap.pj.core.ordemservico.domain.enums.OrdemServicoStatus.EM_DIAGNOSTICO;
import static com.fiap.pj.core.ordemservico.domain.enums.OrdemServicoStatus.EM_EXECUCAO;
import static com.fiap.pj.core.ordemservico.domain.enums.OrdemServicoStatus.FINALIZADA;

@Getter
@AllArgsConstructor
public class OrdemServico {

    private UUID id;
    private UUID clienteId;
    private UUID veiculoId;
    private UUID usuarioId;
    private OrdemServicoStatus status;
    private ZonedDateTime dataCriacao;
    private ZonedDateTime dataConclusao;
    private Diagnostico diagnostico;
    private Set<SituacaoOrdemServico> historicoSituacao = new HashSet<>();
    private Set<Orcamento> orcamentos = new HashSet<>();
    private PagamentoStatus pagamentoStatus;

    @Builder
    public OrdemServico(
            UUID id,
            UUID clienteId,
            UUID veiculoId,
            UUID usuarioId, OrdemServicoStatus status
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.usuarioId = usuarioId;
        this.status = status;
        this.dataCriacao = DateTimeUtils.getNow();
        this.dataConclusao = DateTimeUtils.getNow().plusDays(1);
        this.pagamentoStatus = PagamentoStatus.NAO_INICIADO;
    }

    public void moverParaEmDiagnostico() {
        if (!OrdemServicoStatus.CRIADA.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoDiagnosticoException();
        }
        novaSituacao(EM_DIAGNOSTICO);
    }

    public void moverParaAguardandoAprovacao() {
        if (!OrdemServicoStatus.EM_DIAGNOSTICO.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoAguardandoAprovacaoException();
        }
        novaSituacao(AGUARDANDO_APROVACAO);
    }

    public void moverParaEmExecucao() {
        if (OrdemServicoStatus.AGUARDANDO_APROVACAO.equals(this.status) ||
                OrdemServicoStatus.EM_DIAGNOSTICO.equals(this.status)) {
            novaSituacao(EM_EXECUCAO);
        } else {
            throw new OrdemServicoStatusInvalidoEmExecucaoException();
        }
        this.pagamentoStatus = PagamentoStatus.AGUARDANDO_PAGAMENTO;
    }

    public void moverParaFinalizada() {
        if (!OrdemServicoStatus.EM_EXECUCAO.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoFinalizadaException();
        }
        novaSituacao(FINALIZADA);
    }

    public void moverParaAguardandoRetirada() {
        if (!FINALIZADA.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoAguardandoRetiradaException();
        }
        novaSituacao(OrdemServicoStatus.AGUARDANDO_RETIRADA);
    }

    public void moverParaEntregue() {
        if (!OrdemServicoStatus.AGUARDANDO_RETIRADA.equals(this.status) && !FINALIZADA.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoEntregueException();
        }
        novaSituacao(OrdemServicoStatus.ENTREGUE);
        this.pagamentoStatus = PagamentoStatus.PAGO;
    }

    public void realizarDiagnostico(String descricao) {
        this.validarStatusParaInserirDiagnostico();
        this.diagnostico = new Diagnostico(this.id, descricao);
    }

    public ZonedDateTime getDataCriacaoStatusAtual() {
        return this.getUltimaSituacao().map(SituacaoOrdemServico::getDataCriacao).orElse(ZonedDateTime.now(ZoneOffset.UTC));
    }

    private Optional<SituacaoOrdemServico> getUltimaSituacao() {
        return this.historicoSituacao.stream()
                .max(Comparator.comparing(SituacaoOrdemServico::getDataCriacao));
    }


    private void validarStatusParaInserirDiagnostico() {
        if (!OrdemServicoStatus.EM_DIAGNOSTICO.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoDiagnosticoException();
        }
    }

    private void novaSituacao(OrdemServicoStatus status) {
        var situacao = new SituacaoOrdemServico(status, this.getId());
        this.getHistoricoSituacao().add(situacao);
        this.status = status;
    }


    public void processarPagamento() {
        if (!FINALIZADA.equals(this.status)) {
            throw new OrdemServicoStatusInvalidoFinalizadaException();
        }
        this.pagamentoStatus = PagamentoStatus.PROCESSANDO;
    }

    public BigDecimal getValorTotal() {
        return this.getOrcamentos().stream()
                .map(Orcamento::getValorTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}