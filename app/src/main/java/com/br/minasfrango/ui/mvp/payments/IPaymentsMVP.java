package com.br.minasfrango.ui.mvp.payments;

import android.content.Context;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IPaymentsMVP {

    interface IPresenter {

        void atualizarViewSaldoDevedor();

        void calcularAmortizacaoAutomatica();

        /**
         * @param position do item selecionado na lista de notas abertas
         */
        void calcularArmotizacaoManual(final int position);

        void exibirBotaoGerarRecibo();

        BigDecimal getValorTotalAmortizado();

        boolean creditValueIsGranThenZero();

        int findIdTipoRecebimento(String item);

        Cliente getCliente();

        /**
         * Metodos relacionados a impressao
         */
        void esperarPorConexao();

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                int i, Recebimento recebimento);

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int i, Recebimento recebimento);

        void removerAmortizacao(int position);

        void salvarAmortizacao();

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

        void fecharConexaoAtiva();

        ArrayList<String> loadTipoRecebimentosAVista() throws Throwable;

        boolean saldoDevidoEhMaiorQueZero();

        void setAutomaticNoteSelectionOrder();

        void setClientViews();

        void showInsuficentCredit(String s);

        boolean totalValueOfDebtISLessTranCreditOrEquals();

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);

        void imprimirComprovante();

        void inabilitarBotaoSalvar();

        List<Recebimento> pesquisarRecebimentoPorCliente();
    }

    interface IModel {

        void calculateAmortizationAutomatic();

        void calcularArmotizacaoManual(int position);

        boolean crediValueIsGranThenZero();

        int findIdTipoRecebimento(String item);

        List<Recebimento> pesquisarRecebimentoPorCliente();

        ArrayList<String> loadTipoRecebimentosAVista() throws Throwable;

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                final int posicao, Recebimento recebimentoToUpdate);

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int posicao, Recebimento recebimentoToUpdate);

        void removerAmortizacao(int position);

        boolean saldoDevidoEhMaiorQueZero();

        void salvarAmortizacao();

        void setAutomaticNoteSelectionOrder();

        boolean totalValueOfDebtISLessTranCreditOrEquals();
    }

    interface IView {

        void atualizarViewSaldoDevedor();

        void exibirBotaoGerarRecibo();

        void getParams();

        void inabilitarBotaoSalvarAmortizacao();

        void setClientViews();

        void showInsuficentCredit(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);
    }
}
