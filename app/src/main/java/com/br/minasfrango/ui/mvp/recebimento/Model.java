package com.br.minasfrango.ui.mvp.recebimento;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.br.minasfrango.data.dao.ContaDAO;
import com.br.minasfrango.data.dao.FuncionarioDAO;
import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.model.Conta;
import com.br.minasfrango.data.model.Recebimento;
import com.br.minasfrango.data.realm.ContaORM;
import com.br.minasfrango.data.realm.FuncionarioORM;
import com.br.minasfrango.data.realm.RecebimentoORM;
import com.br.minasfrango.util.ConstantsUtil;
import com.br.minasfrango.util.ControleSessao;
import com.br.minasfrango.util.DateUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/** */
public class Model implements IRecebimentoMVP.IModel {

    IRecebimentoMVP.IPresenter mPresenter;

    ControleSessao mControleSessao;

    ContaDAO mContaDAO = ContaDAO.getInstace(ContaORM.class);

    FuncionarioDAO mFuncionarioDAO = FuncionarioDAO.getInstace(FuncionarioORM.class);

    int position = 0;

    RecebimentoDAO recebimentoDAO = RecebimentoDAO.getInstace(RecebimentoORM.class);

    public Model(final IRecebimentoMVP.IPresenter presenter) {
        mPresenter = presenter;
        this.mControleSessao = new ControleSessao(this.mPresenter.getContext());
    }

    @Override
    public void calcularArmotizacaoManual(final int position) {

        Recebimento recebimentoToUpdate = mPresenter.getRecebimentos().get(position);

        if (creditValueIsGreaterThanZero()
                && creditValueIsGreatherOrEqualThanSalesValueOfItem(recebimentoToUpdate)) {
            recebimentoToUpdate.setValorAmortizado(recebimentoToUpdate.getValorVenda());
            recebimentoToUpdate.setCheck(true);

            mPresenter.getRecebimentos().set(position, recebimentoToUpdate);
            mPresenter.setCredito(
                    mPresenter
                            .getCredito()
                            .subtract(new BigDecimal(recebimentoToUpdate.getValorVenda())));

            mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());

            mPresenter.atualizarRecycleView();

        } else if (creditValueIsGreaterThanZero()
                && creditValueIsLessThanSalesValueOfItem(recebimentoToUpdate)) {
            recebimentoToUpdate.setValorAmortizado(mPresenter.getCredito().doubleValue());
            recebimentoToUpdate.setCheck(true);
            mPresenter.getRecebimentos().set(position, recebimentoToUpdate);

            mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());
            mPresenter.setCredito(new BigDecimal(0));
            mPresenter.atualizarRecycleView();
        }
        mPresenter.showInsuficentCredit("Saldo do  Credito: " + mPresenter.getCredito());
        mPresenter.atualizarViewSaldoDevedor();
    }

    @Override
    public void calcularAmortizacaoAutomatica() {

        // Todos os checkboxs sao desabilitados
        // O funcionarioORM nao pode selecionar nenhuma nota
        // Toda a quitacao e feita automatica

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            mPresenter
                    .getRecebimentos()
                    .forEach(
                            item -> {
                                realizarAmortizacao(item);
                            });
        } else {
            for (Recebimento recebimento : mPresenter.getRecebimentos()) {
                realizarAmortizacao(recebimento);
            }
        }
    }

    @Override
    public boolean saldoDevidoEhMaiorQueZero() {
        return mPresenter
                        .getValorTotalDevido()
                        .subtract(mPresenter.getValorTotalAmortizado())
                        .compareTo(new BigDecimal(0))
                == ConstantsUtil.BIGGER;
    }

    @Override
    public boolean crediValueIsGranThenZero() {
        return mPresenter.getCredito().compareTo(new BigDecimal(0)) == ConstantsUtil.BIGGER;
    }

    @Override
    public List<Conta> pesquisarContaPorId() {
        return this.mContaDAO.pesquisarContaPorId();
    }


    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente() {
        return recebimentoDAO.pesquisarRecebimentoPorCliente(mPresenter.getCliente());
    }

    @Override
    public void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
            final int posicao, Recebimento recebimentoToUpdate) {

        Recebimento recebimentoComMaiorValorDeSelecao = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            recebimentoComMaiorValorDeSelecao =
                    mPresenter.getRecebimentos().stream()
                            .max(Comparator.comparing(Recebimento::getOrderSelected))
                            .get();
        } else {

            Collections.sort(mPresenter.getRecebimentos());
            recebimentoComMaiorValorDeSelecao = mPresenter.getRecebimentos().get(0);
        }
        if (recebimentoComMaiorValorDeSelecao.getOrderSelected() == 0) {
            recebimentoToUpdate.setOrderSelected(1);
        } else {
            recebimentoToUpdate.setOrderSelected(
                    recebimentoComMaiorValorDeSelecao.getOrderSelected() + 1);
        }
        mPresenter.getRecebimentos().set(posicao, recebimentoToUpdate);
    }

    @Override
    public void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
            final int posicao, final Recebimento recebimentoParaAlterar) {

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            mPresenter
                    .getRecebimentos()
                    .forEach(
                            item -> {
                                if (item.getOrderSelected()
                                        > recebimentoParaAlterar.getOrderSelected()) {
                                    item.setOrderSelected(item.getOrderSelected() - 1);

                                    mPresenter
                                            .getRecebimentos()
                                            .set(
                                                    mPresenter.getRecebimentos().lastIndexOf(item),
                                                    item);
                                }
                            });
        } else {

            for (Recebimento recebimento : mPresenter.getRecebimentos()) {

                if (recebimento.getOrderSelected() > recebimentoParaAlterar.getOrderSelected()) {
                    recebimento.setOrderSelected(recebimento.getOrderSelected() - 1);

                    mPresenter
                            .getRecebimentos()
                            .set(
                                    mPresenter.getRecebimentos().lastIndexOf(recebimento),
                                    recebimento);
                }
            }
        }
        // Seto o valor para 0
        recebimentoParaAlterar.setOrderSelected(0);
        // Atualiza a lista
        mPresenter.getRecebimentos().set(posicao, recebimentoParaAlterar);
    }

    @Override
    public void removerAmortizacao(final int position) {

        Recebimento recebimentoToUpdate = mPresenter.getRecebimentos().get(position);
        BigDecimal valorCredito = mPresenter.getCredito();
        BigDecimal valorAmortizadoAnterior =
                new BigDecimal(recebimentoToUpdate.getValorAmortizado());

        recebimentoToUpdate.setValorAmortizado(0);
        recebimentoToUpdate.setCheck(false);
        mPresenter.getRecebimentos().set(position, recebimentoToUpdate);
        mPresenter.processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                position, recebimentoToUpdate);
        mPresenter.updateRecycleViewAlteredItem(position);
        mPresenter.setCredito(valorCredito.add(valorAmortizadoAnterior));
        mPresenter.atualizarViewSaldoDevedor();
        mPresenter.showInsuficentCredit("Saldo Crédito: " + mPresenter.getCredito());
        mPresenter.atualizarViewSaldoDevedor();
        mPresenter.atualizarRecycleView();
    }

    @Override
    public boolean ehMenorOuIgualAoCreditoOValorDoDebito() {
        return ((mPresenter.getCredito().compareTo(mPresenter.getValorTotalDevido())
                        == ConstantsUtil.SMALLER)
                || (mPresenter.getCredito().compareTo(mPresenter.getValorTotalDevido())
                        == ConstantsUtil.EQUAL));
    }

    @Override
    public void salvarAmortizacao() {

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            mPresenter
                    .getRecebimentos()
                    .forEach(
                            item -> {
                                if (item.isCheck()) {

                                    try {
                                        item.setDataRecebimento(
                                                DateUtils.formatarDateParaddMMyyyyhhmm(
                                                        new Date(System.currentTimeMillis())));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    item.setIdConta(String.valueOf(mPresenter.getConta().getId()));
                                    item.setIdNucleo(mControleSessao.getIdNucleo());
                                    recebimentoDAO.alterar(new RecebimentoORM(item));

                                }
                            });
        } else {
            for (Recebimento recebimento : mPresenter.getRecebimentos()) {
                if (recebimento.isCheck()) {
                    try {
                        recebimento.setDataRecebimento(
                                DateUtils.formatarDateParaddMMyyyyhhmm(
                                        new Date(System.currentTimeMillis())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    recebimento.setIdConta(String.valueOf(mPresenter.getConta().getId()));
                    recebimento.setIdNucleo(mControleSessao.getIdNucleo());
                    recebimentoDAO.alterar(new RecebimentoORM(recebimento));


                }
            }
        }
    }



    @Override
    public void setOrdenarSelecaoAutomaticaDasNotas() {}

    private void realizarAmortizacao(final Recebimento item) {
        // Se o valor de credito for maior do que zero e for maior do que o
        // valor devido
        // do item entao o valor amortizado do item recebe o valor da venda
        if (creditValueIsGreaterThanZero()
                && creditValueIsGreatherOrEqualThanSalesValueOfItem(item)) {

            item.setValorAmortizado(item.getValorVenda());
            item.setCheck(true);
            item.setOrderSelected(mPresenter.getRecebimentos().lastIndexOf(item) + 1);
            mPresenter.getRecebimentos().set(mPresenter.getRecebimentos().indexOf(item), item);
            mPresenter.updateRecycleViewAlteredItem(mPresenter.getRecebimentos().lastIndexOf(item));
            mPresenter.setCredito(
                    mPresenter.getCredito().subtract(new BigDecimal(item.getValorVenda())));
            mPresenter.atualizarRecycleView();

        } // Se o valor de credito for maior do que zero e for menor do que o
        // valor devido
        // do item entao o valor amortizado do item recebe o valor do credito
        else if ((creditValueIsGreaterThanZero()) && creditValueIsLessThanSalesValueOfItem(item)) {
            item.setValorAmortizado(mPresenter.getCredito().doubleValue());
            mPresenter.getRecebimentos().set(mPresenter.getRecebimentos().lastIndexOf(item), item);
            item.setCheck(true);
            item.setOrderSelected(mPresenter.getRecebimentos().lastIndexOf(item) + 1);
            mPresenter.setCredito(new BigDecimal(0));
            mPresenter.updateRecycleViewAlteredItem(mPresenter.getRecebimentos().lastIndexOf(item));
            mPresenter.atualizarRecycleView();
        }
    }

    private boolean creditValueIsGreaterThanZero() {
        return mPresenter.getCredito().compareTo(new BigDecimal(0)) == ConstantsUtil.BIGGER;
    }

    private boolean creditValueIsGreatherOrEqualThanSalesValueOfItem(final Recebimento item) {
        return mPresenter.getCredito().compareTo(new BigDecimal(item.getValorVenda()))
                        == ConstantsUtil.BIGGER
                || mPresenter.getCredito().compareTo(new BigDecimal(item.getValorVenda()))
                        == ConstantsUtil.EQUAL;
    }

    private boolean creditValueIsLessThanSalesValueOfItem(final Recebimento item) {
        return mPresenter.getCredito().compareTo(new BigDecimal(item.getValorVenda()))
                == ConstantsUtil.SMALLER;
    }
}
