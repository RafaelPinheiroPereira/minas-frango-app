package com.br.minasfrango.ui.mvp.payments;

import com.br.minasfrango.data.dao.RecebimentoDAO;
import com.br.minasfrango.data.dao.TipoRecebimentoDAO;
import com.br.minasfrango.data.realm.Recebimento;
import com.br.minasfrango.util.ConstantsUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    public void calculateAmortizationAutomatic() {

        mPresenter.setPositionOpenNotaSelect(0);
        // Todos os checkboxs sao desabilitados
        // O funcionario nao pode selecionar nenhuma nota
        // Toda a quitacao e feita automatica

        mPresenter.getRecebimentos()
                .forEach(
                        item->{

                            // Se o valor de credito for maior do que zero e for maior do que o
                            // valor devido
                            // do item entao o valor amortizado do item recebe o valor da venda
                            if (creditValueIsGreaterThanZero()
                                    && creditValueIsGreatherOrEqualThanSalesValueOfItem(item)) {

                                item.setValorAmortizado(item.getValorVenda());
                                item.setCheck(true);
                                item.setOrderSelected(mPresenter.getPositionOpenNotaSelect() + 1);
                                mPresenter.getRecebimentos().set(mPresenter.getPositionOpenNotaSelect(), item);
                                mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());
                                mPresenter.setCredit(
                                        mPresenter
                                                .getCredit()
                                                .subtract(new BigDecimal(item.getValorVenda())));


                            } // Se o valor de credito for maior do que zero e for menor do que o
                            // valor devido
                            // do item entao o valor amortizado do item recebe o valor do credito
                            else if ((creditValueIsGreaterThanZero())
                                    && creditValueIsLessThanSalesValueOfItem(item)) {
                                item.setValorAmortizado(mPresenter.getCredit().doubleValue());
                                mPresenter.getRecebimentos().set(mPresenter.getPositionOpenNotaSelect(), item);
                                item.setCheck(true);
                                mPresenter.setCredit(new BigDecimal(0));
                                mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());
                            }
                            mPresenter.setPositionOpenNotaSelect(mPresenter.getPositionOpenNotaSelect() + 1);
                        });
        // Deve ser zerado pois o objeto ainda fica em instancia

        // mPresenter.setPositionOpenNotaSelect(-1);
        mPresenter.updateRecycleView();


    }

    @Override
    public void calculateAmortizationManually(final int position) {

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


    }

    @Override
    public boolean crediValueIsGranThenZero() {
        return mPresenter.getCredit().compareTo(new BigDecimal(0))
                == ConstantsUtil.BIGGER;

    }

    @Override
    public int findIdTipoRecebimento(final String item) {
        return mTipoRecebimentoDAO.codigoFormaPagamento(item);
    }

    @Override
    public List<Recebimento> loadReceiptsByClient() {
        return recebimentoDAO.findReceiptsByClient(mPresenter.getCliente());
    }

    @Override
    public ArrayList<String> loadTipoRecebimentosAVista() throws Throwable {
        return this.mTipoRecebimentoDAO.carregaFormaPagamentoAmortizacao();
    }

    @Override
    public void removeAmortization(final int position) {

        Recebimento recebimentoToUpdate = mPresenter.getRecebimentos().get(position);
        BigDecimal valorCredito = mPresenter.getCredit();
        BigDecimal valorAmortizadoAnterior = new BigDecimal(recebimentoToUpdate.getValorAmortizado());

        recebimentoToUpdate.setValorAmortizado(0);
        recebimentoToUpdate.setCheck(false);
        mPresenter.getRecebimentos().set(position, recebimentoToUpdate);
        mPresenter.updateRecycleViewAlteredItem(mPresenter.getPositionOpenNotaSelect());
        mPresenter.setCredit(
                valorCredito
                        .add(valorAmortizadoAnterior));
        mPresenter.showInsuficentCredit("Saldo Crédito: " + mPresenter.getCredit());


    }

    @Override
    public void setAutomaticNoteSelectionOrder() {

    }

    @Override
    public boolean totalValueOfDebtISLessTranCreditOrEquals() {
        return ((mPresenter.getCredit().compareTo(mPresenter.getValueTotalDevido())
                == ConstantsUtil.SMALLER) || (mPresenter.getCredit().compareTo(mPresenter.getValueTotalDevido())
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
