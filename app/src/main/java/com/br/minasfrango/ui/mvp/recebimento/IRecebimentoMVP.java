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

        void atualizarRecycleView();

        void exibirBotaoGerarRecibo();

        BigDecimal getValorTotalAmortizado();

        void configurarViewComDadosDoCliente();

        void desabilitarBotaoSalvar();

        Cliente getCliente();

        boolean ehAmortizacaoAutomatica();

        /**
         * Metodos relacionados a impressao
         */
        void esperarPorConexao();

        void exibirBotaoFotografar();

        void removerAmortizacao(int position);

        void salvarAmortizacao();

        void setCliente(Cliente cliente);

        Context getContext();

        int findIdTipoRecebimento(String item);

        BigDecimal getCredit();

        int getIdTipoRecebimento();

        void setIdTipoRecebimento(int idTipoRecebimento);

        void setCredit(BigDecimal credit);

        int getPositionOpenNotaSelect();

        void getParametros();

        List<Recebimento> getRecebimentos();

        BigDecimal getValueTotalDevido();

        List<Recebimento> obterRecebimentoPorCliente();

        ArrayList<String> obterTipoRecebimentos(long id) throws Throwable;

        void fecharConexaoAtiva();

        void processarOrdemDeSelecaoDaNotaAposAmortizacaoManual(
                int i, Recebimento recebimento);

        boolean saldoDevidoEhMaiorQueZero();

        void setAutomaticNoteSelectionOrder();

        void processarOrdemDeSelecaoDaNotaAposRemocaoDaAmortizacao(
                int i, Recebimento recebimento);

        void setPositionOpenNotaSelect(int positionOpenNotaSelect);

        void setTypeOfAmortizationIsAutomatic(boolean typeOfAmortizationIsAutomatic);

        void showInsuficentCredit(String s);

        void updateRecycleViewAlteredItem(int position);

        void imprimirComprovante();

        boolean valorDoCreditoEhMaiorDoQueZero();

        boolean valorTotalDevidoEhMenorOuIgualAoCredito();
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

        void configurarViewComDadosDoCliente();

        void exibirBotaoComprovante();

        void exibirBotaoFotografar();

        void inabilitarBotaoSalvarAmortizacao();

        void getParametros();

        void showInsuficentCredit(String s);

        void updateRecycleView();

        void updateRecycleViewAlteredItem(int position);
    }
}