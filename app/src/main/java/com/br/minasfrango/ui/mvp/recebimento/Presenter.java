package com.br.minasfrango.ui.mvp.recebimento;

import android.app.Activity;
import android.content.Context;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.ui.mvp.recebimento.IRecebimentoMVP.IView;
import com.br.minasfrango.util.ImpressoraUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements IRecebimentoMVP.IPresenter {

    BigDecimal credit = new BigDecimal(0);

    int idTipoRecebimento;

    Cliente mCliente;

    IRecebimentoMVP.IModel mModel;

    ImpressoraUtil mImpressoraUtil;
    /**
     * Posicao da nota selecionada
     */
    int positionOpenNotaSelect = -1;

    List<Recebimento> mRecebimentos = new ArrayList<>();

    boolean typeOfAmortizationIsAutomatic = false;

    BigDecimal valorTotalAmortizado;

    BigDecimal valueTotalDevido;

    IRecebimentoMVP.IView view;

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
    public void atualizarRecycleView() {
        this.view.updateRecycleView();
    }

    @Override
    public void calcularArmotizacaoManual(final int posicaoItem) {

        this.mModel.calcularArmotizacaoManual(posicaoItem);
    }

    @Override
    public void calcularAmortizacaoAutomatica() {
        this.mModel.calculateAmortizationAutomatic();
    }

    @Override
    public void configurarViewComDadosDoCliente() {
        this.view.configurarViewComDadosDoCliente();
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
    public void desabilitarBotaoSalvar() {
        this.view.inabilitarBotaoSalvarAmortizacao();
    }

    @Override
    public boolean ehAmortizacaoAutomatica() {
        return typeOfAmortizationIsAutomatic;
    }

    @Override
    public Cliente getCliente() {
        return mCliente;
    }

    /**
     * Metodos relacionados a impressao
     */
    @Override
    public void esperarPorConexao() {
        if (this.mImpressoraUtil.esperarPorConexao()) {
            this.view.exibirBotaoComprovante();
        } else {
            fecharConexaoAtiva();
        }
    }

    @Override
    public void exibirBotaoFotografar() {
        this.view.exibirBotaoFotografar();
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
    public void exibirBotaoGerarRecibo() {
        this.view.exibirBotaoComprovante();
    }

    @Override
    public void fecharConexaoAtiva() {
        this.mImpressoraUtil.fecharConexaoAtiva();

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
    public int findIdTipoRecebimento(final String item) {
        return this.mModel.findIdTipoRecebimento(item);
    }

    @Override
    public int getPositionOpenNotaSelect() {
        return positionOpenNotaSelect;
    }

    @Override
    public BigDecimal getCredit() {
        return this.credit;
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
    public void setCredit(final BigDecimal credit) {
        this.credit = credit;
    }

    @Override
    public void getParametros() {
        this.view.getParametros();
    }

    @Override
    public BigDecimal getValueTotalDevido() {
        valueTotalDevido =
                new BigDecimal(
                        this.getRecebimentos().stream()
                                .mapToDouble(Recebimento::getValorVenda)
                                .sum());
        return valueTotalDevido;
    }

    @Override
    public void imprimirComprovante() {
        this.mImpressoraUtil.imprimirComprovanteRecebimento(getRecebimentos(), getCliente());

    }

    @Override
    public List<Recebimento> obterRecebimentoPorCliente() {

        return this.mModel.pesquisarRecebimentoPorCliente();
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
    public ArrayList<String> obterTipoRecebimentos(long id) throws Throwable {
        return this.mModel.obterTipoRecebimentos(id);
    }

    @Override
    public void salvarAmortizacao() {

        this.mModel.salvarAmortizacao();


    }

    @Override
    public void setPositionOpenNotaSelect(final int positionOpenNotaSelect) {
        this.positionOpenNotaSelect = positionOpenNotaSelect;
    }

    @Override
    public void setTypeOfAmortizationIsAutomatic(final boolean typeOfAmortizationIsAutomatic) {
        this.typeOfAmortizationIsAutomatic = typeOfAmortizationIsAutomatic;
    }

    @Override
    public void updateRecycleViewAlteredItem(final int position) {
        this.view.updateRecycleViewAlteredItem(position);
    }

    @Override
    public void showInsuficentCredit(final String s) {
        this.view.showInsuficentCredit(s);
    }

    @Override
    public boolean valorDoCreditoEhMaiorDoQueZero() {
        return this.mModel.crediValueIsGranThenZero();
    }

    @Override
    public boolean valorTotalDevidoEhMenorOuIgualAoCredito() {
        return this.mModel.totalValueOfDebtISLessTranCreditOrEquals();
    }
}