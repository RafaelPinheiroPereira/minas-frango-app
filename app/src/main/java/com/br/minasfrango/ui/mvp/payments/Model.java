package com.br.minasfrango.ui.mvp.payments;

import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.realm.Recebimento;
import com.br.minasfrango.util.ConstantsUtil;
import com.br.minasfrango.util.DateUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Model implements IPaymentsMVP.IModel {

    IPaymentsMVP.IPresenter mPresenter;

    TipoRecebimentoDAO mTipoRecebimentoDAO = TipoRecebimentoDAO.getInstace();

    int position = 0;

    RecebimentoDAO recebimentoDAO = RecebimentoDAO.getInstace(Recebimento.class);

    public Model(final IPaymentsMVP.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void calcularArmotizacaoManual(final int position) {

        Recebimento recebimentoToUpdate = mPresenter.getRecebimentos().get(position);

        if (creditValueIsGreaterThanZero()
                && creditValueIsGreatherOrEqualThanSalesValueOfItem(recebimentoToUpdate)) {
            recebimentoToUpdate.setValorAmortizado(recebimentoToUpdate.getValorVenda());
            recebimentoToUpdate.setCheck(true);

            mPresenter.getRecebimentos().set(position, recebimentoToUpdate);
            mPresenter.setCredit(
                    mPresenter
                            .getCredit()
                            .subtract(new BigDecimal(recebimentoToUpdate.getValorVenda())));

            mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());

            mPresenter.updateRecycleView();

        } else if (creditValueIsGreaterThanZero()
                && creditValueIsLessThanSalesValueOfItem(recebimentoToUpdate)) {
            recebimentoToUpdate.setValorAmortizado(mPresenter.getCredit().doubleValue());
            recebimentoToUpdate.setCheck(true);
            mPresenter.getRecebimentos().set(position, recebimentoToUpdate);

            mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());
            mPresenter.setCredit(new BigDecimal(0));
            mPresenter.updateRecycleView();
        }
        mPresenter.showInsuficentCredit("Saldo do  Credito: " + mPresenter.getCredit());
        mPresenter.atualizarViewSaldoDevedor();
    }

    @Override
    public void calculateAmortizationAutomatic() {

        // Todos os checkboxs sao desabilitados
        // O funcionario nao pode selecionar nenhuma nota
        // Toda a quitacao e feita automatica

        mPresenter
                .getRecebimentos()
                .forEach(
                        item-> {

                            // Se o valor de credito for maior do que zero e for maior do que o
                            // valor devido
                            // do item entao o valor amortizado do item recebe o valor da venda
                            if (creditValueIsGreaterThanZero()
                                    && creditValueIsGreatherOrEqualThanSalesValueOfItem(item)) {

                                item.setValorAmortizado(item.getValorVenda());
                                item.setCheck(true);
                                item.setOrderSelected(
                                        mPresenter.getRecebimentos().lastIndexOf(item) + 1);
                                mPresenter
                                        .getRecebimentos()
                                        .set(mPresenter.getRecebimentos().indexOf(item), item);
                                mPresenter.updateRecycleViewAlteredItem(
                                        mPresenter.getRecebimentos().lastIndexOf(item));
                                mPresenter.setCredit(
                                        mPresenter
                                                .getCredit()
                                                .subtract(new BigDecimal(item.getValorVenda())));
                                mPresenter.updateRecycleView();

                            } // Se o valor de credito for maior do que zero e for menor do que o
                            // valor devido
                            // do item entao o valor amortizado do item recebe o valor do credito
                            else if ((creditValueIsGreaterThanZero())
                                    && creditValueIsLessThanSalesValueOfItem(item)) {
                                item.setValorAmortizado(mPresenter.getCredit().doubleValue());
                                mPresenter
                                        .getRecebimentos()
                                        .set(mPresenter.getRecebimentos().lastIndexOf(item), item);
                                item.setCheck(true);
                                item.setOrderSelected(
                                        mPresenter.getRecebimentos().lastIndexOf(item) + 1);
                                mPresenter.setCredit(new BigDecimal(0));
                                mPresenter.updateRecycleViewAlteredItem(
                                        mPresenter.getRecebimentos().lastIndexOf(item));
                                mPresenter.updateRecycleView();
                            }
                        });
    }

    @Override
    public boolean crediValueIsGranThenZero() {
        return mPresenter.getCredit().compareTo(new BigDecimal(0)) == ConstantsUtil.BIGGER;
    }

    @Override
    public int findIdTipoRecebimento(final String item) {
        return mTipoRecebimentoDAO.codigoFormaPagamento(item);
    }

    @Override
    public List<Recebimento> pesquisarRecebimentoPorCliente() {
        return recebimentoDAO.pesquisarRecebimentoPorCliente(mPresenter.getCliente());
    }

    @Override
    public ArrayList<String> loadTipoRecebimentosAVista() throws Throwable {
        return this.mTipoRecebimentoDAO.carregaFormaPagamentoAmortizacao();
    }

    @Override
    public void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
            final int posicao, Recebimento recebimentoToUpdate) {
        Recebimento recebimentoComMaiorValorDeSelecao =
                mPresenter.getRecebimentos().stream()
                        .max(Comparator.comparing(Recebimento::getOrderSelected))
                        .get();
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
            final int posicao, final Recebimento recebimentoToUpdate) {

        mPresenter
                .getRecebimentos()
                .forEach(
                        item-> {
                            if (item.getOrderSelected() > recebimentoToUpdate.getOrderSelected()) {
                                item.setOrderSelected(item.getOrderSelected() - 1);

                                mPresenter
                                        .getRecebimentos()
                                        .set(mPresenter.getRecebimentos().lastIndexOf(item), item);
                            }
                        });
        // Seto o valor para 0
        recebimentoToUpdate.setOrderSelected(0);
        // Atualiza a lista
        mPresenter.getRecebimentos().set(posicao, recebimentoToUpdate);
    }

    @Override
    public void removerAmortizacao(final int position) {

        Recebimento recebimentoToUpdate = mPresenter.getRecebimentos().get(position);
        BigDecimal valorCredito = mPresenter.getCredit();
        BigDecimal valorAmortizadoAnterior =
                new BigDecimal(recebimentoToUpdate.getValorAmortizado());

        recebimentoToUpdate.setValorAmortizado(0);
        recebimentoToUpdate.setCheck(false);
        mPresenter.getRecebimentos().set(position, recebimentoToUpdate);
        mPresenter.processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                position, recebimentoToUpdate);
        mPresenter.updateRecycleViewAlteredItem(position);
        mPresenter.setCredit(valorCredito.add(valorAmortizadoAnterior));
        mPresenter.atualizarViewSaldoDevedor();
        mPresenter.showInsuficentCredit("Saldo Crédito: " + mPresenter.getCredit());
        mPresenter.atualizarViewSaldoDevedor();
        mPresenter.updateRecycleView();
    }

    @Override
    public boolean saldoDevidoEhMaiorQueZero() {
        return mPresenter
                .getValueTotalDevido()
                .subtract(mPresenter.getValorTotalAmortizado())
                .compareTo(new BigDecimal(0))
                == ConstantsUtil.BIGGER;
    }

    @Override
    public void salvarAmortizacao() {

        mPresenter
                .getRecebimentos()
                .forEach(
                        item->{
                            if (item.isCheck()) {

                                try {
                                    item.setDataRecebimento(
                                            DateUtils.formatarDateddMMyyyyhhmm(
                                                    new Date(System.currentTimeMillis())));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                recebimentoDAO.copyOrUpdate(item);
                            }
                        });
    }

    @Override
    public void setAutomaticNoteSelectionOrder() {}

    @Override
    public boolean totalValueOfDebtISLessTranCreditOrEquals() {
        return ((mPresenter.getCredit().compareTo(mPresenter.getValueTotalDevido())
                == ConstantsUtil.SMALLER)
                || (mPresenter.getCredit().compareTo(mPresenter.getValueTotalDevido())
                == ConstantsUtil.EQUAL));
    }

    private boolean creditValueIsGreaterThanZero() {
        return mPresenter.getCredit().compareTo(new BigDecimal(0)) == ConstantsUtil.BIGGER;
    }

    private boolean creditValueIsGreatherOrEqualThanSalesValueOfItem(final Recebimento item) {
        return mPresenter.getCredit().compareTo(new BigDecimal(item.getValorVenda()))
                == ConstantsUtil.BIGGER
                || mPresenter.getCredit().compareTo(new BigDecimal(item.getValorVenda()))
                == ConstantsUtil.EQUAL;
    }

    private boolean creditValueIsLessThanSalesValueOfItem(final Recebimento item) {
        return mPresenter.getCredit().compareTo(new BigDecimal(item.getValorVenda()))
                == ConstantsUtil.SMALLER;
    }
}
