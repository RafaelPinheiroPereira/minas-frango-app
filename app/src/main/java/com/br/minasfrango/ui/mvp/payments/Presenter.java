package com.br.minasfrango.ui.mvp.payments;

import android.content.Context;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Recebimento;
import com.br.minasfrango.ui.mvp.payments.IPaymentsMVP.IView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements IPaymentsMVP.IPresenter {

    BigDecimal credit = new BigDecimal(0);

    int idTipoRecebimento;

    Cliente mCliente;

    IPaymentsMVP.IModel mModel;

    /**
     * Posicao da nota selecionada
     */
    int positionOpenNotaSelect = -1;

    List<Recebimento> recebimentos = new ArrayList<>();

    boolean typeOfAmortizationIsAutomatic = false;

    BigDecimal valorTotalAmortizado;

    BigDecimal valueTotalDevido;

    IPaymentsMVP.IView view;

    public Presenter(final IView view) {
        this.view = view;
        this.mModel = new Model(this);
    }

    @Override
    public void atualizarViewSaldoDevedor() {
        this.view.atualizarViewSaldoDevedor();
    }

    @Override
    public void calcularAmortizacaoAutomatica() {
        this.mModel.calculateAmortizationAutomatic();
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
    public boolean creditValueIsGranThenZero() {
        return this.mModel.crediValueIsGranThenZero();
    }

    @Override
    public int findIdTipoRecebimento(final String item) {
        return this.mModel.findIdTipoRecebimento(item);
    }

    @Override
    public Cliente getCliente() {
        return mCliente;
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
    public BigDecimal getCredit() {
        return this.credit;
    }

    @Override
    public void setCredit(final BigDecimal credit) {
        this.credit = credit;
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
    public void setPositionOpenNotaSelect(final int positionOpenNotaSelect) {
        this.positionOpenNotaSelect = positionOpenNotaSelect;
    }

    @Override
    public List<Recebimento> getRecebimentos() {
        return recebimentos;
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
    public boolean isTypeOfAmortizationIsAutomatic() {
        return typeOfAmortizationIsAutomatic;
    }

    @Override
    public void setTypeOfAmortizationIsAutomatic(final boolean typeOfAmortizationIsAutomatic) {
        this.typeOfAmortizationIsAutomatic = typeOfAmortizationIsAutomatic;
    }

    @Override
    public List<Recebimento> loadReceiptsByClient() {

        return this.mModel.loadReceiptsByClient();
    }

    @Override
    public ArrayList<String> loadTipoRecebimentosAVista() throws Throwable {
        return this.mModel.loadTipoRecebimentosAVista();
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
    public void setClientViews() {
        this.view.setClientViews();
    }

    @Override
    public void showInsuficentCredit(final String s) {
        this.view.showInsuficentCredit(s);
    }

    @Override
    public boolean totalValueOfDebtISLessTranCreditOrEquals() {
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
}
