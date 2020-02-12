package com.br.minasfrango.ui.mvp.recebimento;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.model.BlocoRecibo;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.ui.mvp.recebimento.IRecebimentoMVP.IView;
import com.br.minasfrango.util.DriveServiceHelper;
import com.br.minasfrango.util.ImpressoraUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presenter implements IRecebimentoMVP.IPresenter {

    BigDecimal credit = new BigDecimal(0);

    Conta conta;

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

    BigDecimal valorTotalDevido;

    IRecebimentoMVP.IView view;

    DriveServiceHelper mDriveServiceHelper;

    BlocoRecibo mBlocoRecibo;



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
        this.mModel.calcularAmortizacaoAutomatica();
    }

    @Override
    public void configurarViewComDadosDoCliente() {
        this.view.configurarViewComDadosDoCliente();
    }

    @Override
    public BigDecimal getValorTotalAmortizado() {

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            return this.valorTotalAmortizado =
                    new BigDecimal(
                            getRecebimentos().stream()
                                    .mapToDouble(Recebimento::getValorAmortizado)
                                    .sum());
        } else {
            double valorAmortizado = 0.0;
            for (Recebimento recebimento : this.getRecebimentos()) {
                valorAmortizado += recebimento.getValorAmortizado();
            }

            return this.valorTotalAmortizado = new BigDecimal(valorAmortizado);
        }
    }

    @Override
    public void desabilitarBotaoSalvar() {
        this.view.desabilitarBotaoSalvarAmortizacao();
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
    public long configurarSequenceDoRecebimento() {
        return this.mModel.configurarSequenceDoRecebimento();
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
    public void setConta(Conta conta) {
        this.conta = conta;
    }



    @Override
    public int getPositionOpenNotaSelect() {
        return positionOpenNotaSelect;
    }

    @Override
    public BigDecimal getCredito() {
        return this.credit;
    }

    @Override
    public Conta getConta() {
        return this.conta;
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
    public void setCredito(final BigDecimal credit) {
        this.credit = credit;
    }

    @Override
    public void getParametros() {
        this.view.getParametros();
    }

    @Override
    public DriveServiceHelper getDriveServiceHelper() {
        return mDriveServiceHelper;
    }
    @Override
    public void setDriveServiceHelper(final DriveServiceHelper driveServiceHelper) {
        mDriveServiceHelper = driveServiceHelper;
    }

    @Override
    public BigDecimal getValorTotalDevido() {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            valorTotalDevido =
                    new BigDecimal(
                            this.getRecebimentos().stream()
                                    .mapToDouble(Recebimento::getValorVenda)
                                    .sum());
        } else {
            double valorVenda = 0.0;
            for (Recebimento recebimento : this.getRecebimentos()) {
                valorVenda += recebimento.getValorVenda();
            }
            valorTotalDevido = new BigDecimal(valorVenda);
        }
        return valorTotalDevido;
    }

    @Override
    public void imprimirComprovante() {
        this.mImpressoraUtil.imprimirComprovanteRecebimento(getRecebimentos(), getCliente());

    }



    @Override
    public boolean saldoDevidoEhMaiorQueZero() {
        return this.mModel.saldoDevidoEhMaiorQueZero();
    }

    @Override
    public void setAutomaticNoteSelectionOrder() {
        this.mModel.setOrdenarSelecaoAutomaticaDasNotas();
    }



    @Override
    public void salvarAmortizacao(final long idBlocoRecibo) {

        this.mModel.salvarAmortizacao(idBlocoRecibo);


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
        return this.mModel.ehMenorOuIgualAoCreditoOValorDoDebito();
    }

    @Override
    public void alterarBlocoRecibo(final BlocoRecibo blocoRecibo) {
        this.mModel.alterarBlocoRecibo(blocoRecibo);
    }

    @Override
    public List<Recebimento> obterRecebimentoPorCliente() {

        return this.mModel.pesquisarRecebimentoPorCliente();
    }
    @Override
    public List<Conta> pesquisarContaPorId() {
        return this.mModel.pesquisarContaPorId();
    }

    @Override
    public void verificarCredenciaisGoogleDrive() {
        this.view.verificarCredenciaisGoogleDrive();
    }


    @Override
    public BlocoRecibo getBlocoRecibo() {
        return mBlocoRecibo;
    }
    @Override
    public void setBlocoRecibo(final BlocoRecibo blocoRecibo) {
        mBlocoRecibo = blocoRecibo;
    }
}