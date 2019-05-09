package com.br.minasfrango.ui.mvp.recebimento;

import android.app.Activity;
import android.content.Context;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.ui.mvp.recebimento.IPaymentsMVP.IView;
import com.br.minasfrango.util.ImpressoraUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements IPaymentsMVP.IPresenter {

    boolean amortizacaoAutomatica = false;

    int idTipoRecebimento;

    Cliente mCliente;

    IPaymentsMVP.IModel mModel;

    ImpressoraUtil mImpressoraUtil;

    /**
     * Posicao da nota selecionada
     */
    int positionOpenNotaSelect = -1;

    List<Recebimento> mRecebimentos = new ArrayList<>();

    BigDecimal valorCredito = new BigDecimal(0);

    BigDecimal valorTotalAmortizado;

    BigDecimal valorTotalDevido;

    IPaymentsMVP.IView view;

    public Presenter(final IView view) {
        this.view = view;
        this.mModel = new Model(this);
        this.mImpressoraUtil = new ImpressoraUtil((Activity) getContext());
    }

    @Override
    public void atualizarViewSaldoDevedor() {
        this.view.atualizarViewSaldoDevedor();
    }

    @Override
    public void calcularAmortizacaoAutomatica() {
        this.mModel.calcularAmortizacaoAutomatica();
    }

    @Override
    public void calcularArmotizacaoManual(final int posicaoItem) {

        this.mModel.calcularArmotizacaoManual(posicaoItem);
    }

    @Override
    public void exibirBotaoGerarRecibo() {
        this.view.exibirBotaoGerarRecibo();
    }

    @Override
    public BigDecimal getValorTotalAmortizado() {

        return this.valorTotalAmortizado =
                new BigDecimal(
                        getRecebimentos().stream()
                                .mapToDouble(Recebimento::getValorAmortizado)
                                .sum());
    }

    @Override
    public void carregarDadosClienteView() {
        this.view.setClientViews();
    }

    @Override
    public ArrayList<String> carregarDescricaoTipoRecebimentosAVista() throws Throwable {
        return this.mModel.obterTipoRecebimentosAVista();
    }

    @Override
    public Cliente getCliente() {
        return mCliente;
    }

    /** Metodos relacionados a impressao */
    @Override
    public void esperarPorConexao() {
        if (this.mImpressoraUtil.esperarPorConexao()) {
            this.view.exibirBotaoGerarRecibo();
        }
    }

    @Override
    public void exibirMensagemDeSaldoInsuficiente(final String s) {
        this.view.showCreditoInsuficiente(s);
    }

    @Override
    public void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
            final int posicao, final Recebimento recebimento) {
        this.mModel.processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(posicao, recebimento);
    }

    @Override
    public void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
            final int posicao, final Recebimento recebimento) {
        this.mModel.processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(posicao, recebimento);
    }

    @Override
    public void setCliente(final Cliente cliente) {
        mCliente = cliente;
    }

    @Override
    public Context getContext() {
        return (Context) this.view;
    }

    @Override
    public BigDecimal getValorCredito() {
        return this.valorCredito;
    }

    @Override
    public void setValorCredito(final BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    @Override
    public int getIdTipoRecebimento() {
        return idTipoRecebimento;
    }

    @Override
    public void setIdTipoRecebimento(final int idTipoRecebimento) {
        this.idTipoRecebimento = idTipoRecebimento;
    }

    @Override
    public void getParams() {
        this.view.getParams();
    }

    @Override
    public int getPositionOpenNotaSelect() {
        return positionOpenNotaSelect;
    }



    @Override
    public List<Recebimento> getRecebimentos() {
        return mRecebimentos;
    }

    @Override
    public void removerAmortizacao(final int position) {
        this.mModel.removerAmortizacao(position);
    }

    @Override
    public void salvarAmortizacao() {

        this.mModel.salvarAmortizacao();
    }

    @Override
    public BigDecimal getValorTotalDevido() {
        valorTotalDevido =
                new BigDecimal(
                        this.getRecebimentos().stream()
                                .mapToDouble(Recebimento::getValorVenda)
                                .sum());
        return valorTotalDevido;
    }

    @Override
    public boolean isAmortizacaoAutomatica() {
        return amortizacaoAutomatica;
    }

    @Override
    public void fecharConexaoAtiva() {
        this.mImpressoraUtil.fecharConexaoAtiva();
    }

    @Override
    public void setAmortizacaoAutomatica(final boolean amortizacaoAutomatica) {
        this.amortizacaoAutomatica = amortizacaoAutomatica;
    }

    @Override
    public boolean saldoDevidoEhMaiorQueZero() {
        return this.mModel.saldoDevidoEhMaiorQueZero();
    }

    @Override
    public void setAutomaticNoteSelectionOrder() {
        this.mModel.setAutomaticNoteSelectionOrder();
    }

    @Override
    public int pesquisarIdTipoRecebimento(final String item) {
        return this.mModel.pesquisarIdTipoRecebimento(item);
    }

    @Override
    public boolean valorCreditoEhMaiorQueZero() {
        return this.mModel.valorCreditoEhMaiorQueZero();
    }

    @Override
    public boolean valorTotalDebitoEhMenorOuIgualAoCredito() {
        return this.mModel.totalValueOfDebtISLessTranCreditOrEquals();
    }

    @Override
    public void updateRecycleView() {
        this.view.updateRecycleView();
    }

    @Override
    public void updateRecycleViewAlteredItem(final int position) {
        this.view.updateRecycleViewAlteredItem(position);
    }

    @Override
    public void imprimirComprovante() {
        this.mImpressoraUtil.imprimirComprovanteRecebimento(getRecebimentos(), getCliente());
    }

    @Override
    public void inabilitarBotaoSalvar() {
        this.view.inabilitarBotaoSalvarAmortizacao();
    }

    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente() {

        return this.mModel.pesquisarRecebimentoPorCliente();
    }
}
