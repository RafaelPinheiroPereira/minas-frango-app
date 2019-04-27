package com.br.minasfrango.ui.mvp.payments;

import android.content.Context;
import com.br.minasfrango.data.realm.Cliente;
import com.br.minasfrango.data.realm.Recebimento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentsMVP {

    interface IPresenter {

        void calculateAmortizationAutomatic();

        void calculateAmortizationManually(final int position);

        boolean creditValueIsGranThenZero();

        int findIdTipoRecebimento(String item);

        Cliente getCliente();

        void setCliente(Cliente cliente);

        Context getContext();

        BigDecimal getCredit();

        void setCredit(BigDecimal credit);

        int getIdTipoRecebimento();

        void setIdTipoRecebimento(int idTipoRecebimento);

        void getParams();

        int getPositionOpenNotaSelect();

        void setPositionOpenNotaSelect(int positionOpenNotaSelect);

        List<Recebimento> getRecebimentos();

        BigDecimal getValueTotalDevido();

        boolean isTypeOfAmortizationIsAutomatic();

        void setTypeOfAmortizationIsAutomatic(boolean typeOfAmortizationIsAutomatic);

        List<Recebimento> loadReceiptsByClient();

        ArrayList<String> loadTipoRecebimentosAVista() throws Throwable;

        void removeAmortization(int position);

        void setAutomaticNoteSelectionOrder();

        void setClientViews();

        void showInsuficentCredit(String s);

        boolean totalValueOfDebtISLessTranCreditOrEquals();

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);

    }

    interface IModel {

        void calculateAmortizationAutomatic();

        void calculateAmortizationManually(int position);

        boolean crediValueIsGranThenZero();

        int findIdTipoRecebimento(String item);

        List<Recebimento> loadReceiptsByClient();

        ArrayList<String> loadTipoRecebimentosAVista() throws Throwable;

        void removeAmortization(int position);

        void setAutomaticNoteSelectionOrder();

        boolean totalValueOfDebtISLessTranCreditOrEquals();
    }

    interface IView {

        void getParams();

        void setClientViews();

        void showInsuficentCredit(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);
    }


}
