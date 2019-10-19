package com.br.minasfrango.ui.mvp.recebimento;

import android.content.Context;
import com.br.minasfrango.data.model.Cliente;
import com.br.minasfrango.data.model.Recebimento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface IRecebimentoMVP {

    interface IPresenter {

        void atualizarViewSaldoDevedor();

        void calcularAmortizacaoAutomatica();

        /**
         * @param position do item selecionado na lista de notas abertas
         */
        void calcularArmotizacaoManual(final int position);

        void exibirBotaoFotografar();

        void exibirBotaoGerarRecibo();

        BigDecimal getValorTotalAmortizado();

        boolean valorDoCreditoEhMaiorDoQueZero();

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

        void getParametros();

        int getPositionOpenNotaSelect();

        void setPositionOpenNotaSelect(int positionOpenNotaSelect);

        List<Recebimento> getRecebimentos();

        BigDecimal getValueTotalDevido();

        boolean ehAmortizacaoAutomatica();

        void setTypeOfAmortizationIsAutomatic(boolean typeOfAmortizationIsAutomatic);

        void fecharConexaoAtiva();

        ArrayList<String> obterTipoRecebimentos(long id) throws Throwable;

        boolean saldoDevidoEhMaiorQueZero();

        void setAutomaticNoteSelectionOrder();

        void configurarViewComDadosDoCliente();

        void showInsuficentCredit(String s);

        boolean valorTotalDevidoEhMenorOuIgualAoCredito();

        void atualizarRecycleView();

        void updateRecycleViewAlteredItem(int position);

        void imprimirComprovante();

        void desabilitarBotaoSalvar();

        List<Recebimento> obterRecebimentoPorCliente();
    }

    interface IModel {

        void calculateAmortizationAutomatic();

        void calcularArmotizacaoManual(int position);

        boolean crediValueIsGranThenZero();

        int findIdTipoRecebimento(String item);

        List<Recebimento> pesquisarRecebimentoPorCliente();

        ArrayList<String> obterTipoRecebimentos(long id) throws Throwable;

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

        void exibirBotaoFotografar();

        void exibirBotaoComprovante();

        void getParametros();

        void inabilitarBotaoSalvarAmortizacao();

        void configurarViewComDadosDoCliente();

        void showInsuficentCredit(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);
    }
}
